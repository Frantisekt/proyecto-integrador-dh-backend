package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.UserRequestDTO;
import com.backend.globeonclick.dto.response.FavoritePackagesResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.dto.response.UserResponseDTO;
import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.entity.User;
import com.backend.globeonclick.exception.ResourceNotFoundException;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.repository.IUserRepository;
import com.backend.globeonclick.services.interfaces.IUserService;
import com.backend.globeonclick.utils.mappers.TourPackageMapper;
import com.backend.globeonclick.utils.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final ITourPackageRepository tourPackageRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TourPackageMapper tourPackageMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (existsByDni(userDTO.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        User user = userMapper.toEntity(userDTO);
        
        // Establecer rol por defecto si no viene especificado
        if (userDTO.getRole() == null) {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar email único si está cambiando
        if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Verificar DNI único si está cambiando
        if (!user.getDni().equals(userDTO.getDni()) && existsByDni(userDTO.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        userMapper.updateEntity(user, userDTO);

        // Validar que el rol sea válido
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userRepository.existsByDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByDni(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + dni));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public void addFavoritePackage(Long userId, Long packageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado con ID: " + packageId));

        user.addFavoritePackage(tourPackage);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFavoritePackage(Long userId, Long packageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado con ID: " + packageId));

        user.removeFavoritePackage(tourPackage);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoritePackagesResponseDTO getUserFavoritePackages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Cargar explícitamente los favoritos para evitar LazyInitializationException
        List<TourPackage> favoritePackages = entityManager.createQuery(
                        "SELECT tp FROM TourPackage tp JOIN tp.favoriteByUsers u WHERE u.userId = :userId",
                        TourPackage.class)
                .setParameter("userId", userId)
                .getResultList();

        List<TourPackageResponseDTO> favoritePackageDTOs = favoritePackages.stream()
                .map(tourPackageMapper::toResponseDTO)
                .collect(Collectors.toList());

        return FavoritePackagesResponseDTO.builder()
                .favoritePackages(favoritePackageDTOs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourPackageResponseDTO> getUserFavoritePackagesPaginated(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Primero, obtener los IDs para paginación
        Page<Long> favoritePackageIds = userRepository.findFavoritePackageIds(userId, pageable);

        if (favoritePackageIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Luego, cargar los paquetes completos con datos relacionados
        List<TourPackage> favoritePackages = loadTourPackagesWithRelatedData(favoritePackageIds.getContent());

        // Convertir a DTOs
        List<TourPackageResponseDTO> favoritePackageDTOs = favoritePackages.stream()
                .map(tourPackageMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(favoritePackageDTOs, pageable, favoritePackageIds.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPackageFavorite(Long userId, Long packageId) {
        return userRepository.existsByUserIdAndFavoritePackagesPackageId(userId, packageId);
    }

    private List<TourPackage> loadTourPackagesWithRelatedData(List<Long> packageIds) {
        if (packageIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Primero cargar los paquetes con categorías
        List<TourPackage> packages = entityManager.createQuery(
                        "SELECT DISTINCT tp FROM TourPackage tp " +
                                "LEFT JOIN FETCH tp.categories c " +
                                "WHERE tp.packageId IN :ids", TourPackage.class)
                .setParameter("ids", packageIds)
                .getResultList();

        Map<Long, TourPackage> packageMap = packages.stream()
                .collect(Collectors.toMap(TourPackage::getPackageId, tp -> tp));

        // Luego cargar características
        entityManager.createQuery(
                        "SELECT DISTINCT tp FROM TourPackage tp " +
                                "LEFT JOIN FETCH tp.features " +
                                "WHERE tp.packageId IN :ids", TourPackage.class)
                .setParameter("ids", packageIds)
                .getResultList()
                .forEach(tp -> {
                    TourPackage existingPackage = packageMap.get(tp.getPackageId());
                    if (existingPackage != null) {
                        existingPackage.setFeatures(tp.getFeatures());
                    }
                });

        // Finalmente cargar media packages limitados a 20 por paquete
        Map<Long, List<MediaPackage>> mediaPackagesMap = new HashMap<>();
        List<MediaPackage> mediaPackages = entityManager.createQuery(
                        "SELECT mp FROM MediaPackage mp " +
                                "JOIN mp.tourPackages tp " +
                                "JOIN FETCH mp.media " +
                                "WHERE tp.packageId IN :ids " +
                                "ORDER BY mp.mediaPackageId", MediaPackage.class)
                .setParameter("ids", packageIds)
                .setMaxResults(packageIds.size() * 20)
                .getResultList();

        for (MediaPackage mp : mediaPackages) {
            for (TourPackage tp : mp.getTourPackages()) {
                if (packageIds.contains(tp.getPackageId())) {
                    mediaPackagesMap.computeIfAbsent(tp.getPackageId(), k -> new ArrayList<>())
                            .add(mp);
                }
            }
        }

        mediaPackagesMap.forEach((packageId, mps) -> {
            TourPackage tp = packageMap.get(packageId);
            if (tp != null) {
                tp.setMediaPackages(mps.size() <= 20 ? mps : mps.subList(0, 20));
            }
        });

        // Ordenar según el orden original de packageIds para mantener consistencia con paginación
        return packageIds.stream()
                .map(packageMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}