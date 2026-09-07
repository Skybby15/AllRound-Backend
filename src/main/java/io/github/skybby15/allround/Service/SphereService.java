package io.github.skybby15.allround.Service;

import java.util.List;

import io.github.skybby15.allround.DTO.Filters.SphereFilter;
import io.github.skybby15.allround.DTO.Sphere.CreateSphereRequest;
import io.github.skybby15.allround.DTO.Sphere.CreateSphereResponse;
import io.github.skybby15.allround.DTO.Sphere.ListSphereResponse;
import io.github.skybby15.allround.DTO.Sphere.SphereListInfoDTO;
import io.github.skybby15.allround.Exception.ApiException;
import io.github.skybby15.allround.Exception.InvalidSphereException;
import io.github.skybby15.allround.Exception.UserNotExistingException;
import io.github.skybby15.allround.Model.Sphere;
import io.github.skybby15.allround.Model.User;
import io.github.skybby15.allround.Repository.SphereRepository;
import io.github.skybby15.allround.Repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@ApplicationScoped 
public class SphereService {
    @Inject  SphereRepository sphereRepository;
    @Inject UserRepository userRepository;
    
    @Transactional 
    public CreateSphereResponse createSphere(CreateSphereRequest request, Long userId) throws ApiException {
        User user = userRepository.findByIdOptional(userId).orElseThrow(
            () -> new UserNotExistingException()
        );
        
        Sphere newSphere = Sphere.builder()
            .owner(user)
            .name(request.name())
            .build();

        try{
            sphereRepository.persist(newSphere);
        }catch(PersistenceException e){
            throw new InvalidSphereException(e.getMessage());
        }


        return CreateSphereResponse.builder()
            .sphereId(newSphere.getId())
            .build();
    }

    public ListSphereResponse getSpheresFiltered(SphereFilter filter){
        
        List<SphereListInfoDTO> spheres = sphereRepository.findByFilter(filter)
        .stream()
        .map(sphere -> SphereListInfoDTO.builder()
            .id(sphere.getId())
            .ownerId(sphere.getOwner().getId())
            .name(sphere.getName())
            .build()
        ).toList();

        return ListSphereResponse.builder()
            .spheres(spheres)
            .build();
    }
}
