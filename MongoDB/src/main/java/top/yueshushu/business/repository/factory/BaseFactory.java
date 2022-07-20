package top.yueshushu.business.repository.factory;

import java.util.Collection;
import java.util.List;

/**
 * @author yuejianli
 * @date 2022-07-20
 */
public interface BaseFactory<DO, Entity> {

    /**
     * convert to model
     * @param entity dto对象
     * @return model对象
     */
    DO toModel(Entity entity);

    /**
     * convert to dto
     * @param DO model对象
     * @return dto对象
     */
    Entity toDTO(DO DO);

    /**
     * convert to model
     * @param entities dto对象
     * @return model对象
     */
    List<DO> toModel(Collection<Entity> entities);

    /**
     * convert to dto
     * @param DOS model对象
     * @return dto对象
     */
    List<Entity> toDTO(Collection<DO> DOS);
}
