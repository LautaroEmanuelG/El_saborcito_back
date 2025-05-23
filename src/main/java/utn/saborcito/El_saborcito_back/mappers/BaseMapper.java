package utn.saborcito.El_saborcito_back.mappers;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper<E, D> {
    D toDTO(E source);

    E toEntity(D source);

    default List<D> toDTOList(List<E> sourceList) {
        if (sourceList == null) {
            return null;
        }
        return sourceList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    default List<E> toEntityList(List<D> sourceList) {
        if (sourceList == null) {
            return null;
        }
        return sourceList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
