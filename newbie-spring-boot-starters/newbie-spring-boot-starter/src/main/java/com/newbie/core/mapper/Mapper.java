package com.newbie.core.mapper;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

@Getter
@Setter
public class Mapper {
    private static Mapper mapper;
    private ModelMapper modelMapper;
    private Mapper (){}
    public static synchronized <T> Mapper init() {
        if (mapper == null) {
            mapper = new Mapper();
            mapper.setModelMapper(new ModelMapper());
        }
        return mapper;
    }

    public <D> D map(Object source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }


    public <S, D> ModelMapper customMappings(PropertyMap<S, D> propertyMap) {
        this.modelMapper.addMappings(propertyMap);
        return this.modelMapper;
    }

    public <D> D map(Object source, GenericsToken<D> destination) {
        return modelMapper.map(source, destination.getType());
    }
}
