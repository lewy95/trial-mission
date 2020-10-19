package cn.xzxy.lewy.quartz.dao;

import cn.xzxy.lewy.quartz.model.TbJobEntity;
import org.springframework.data.repository.CrudRepository;

public interface TbJobEntityRepository extends CrudRepository<TbJobEntity, Long> {
    TbJobEntity getById(Integer id);
}