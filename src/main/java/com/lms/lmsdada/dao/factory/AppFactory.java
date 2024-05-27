package com.lms.lmsdada.dao.factory;

import com.lms.lmsdada.dao.entity.App;
import com.lms.lmsdada.dao.vo.AppVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * app字段转换类
 *
 * @author LMS2000
 * @since 2024-05-23
 */
public class AppFactory {
    public static final AppFactory.AppConverter APP_CONVERTER = Mappers.getMapper(AppFactory.AppConverter.class);

    @Mapper
    public interface AppConverter {
        @Mappings({

        })
        AppVO toAppVO(App entity);

        List<AppVO> toListAppVO(List<App> entityList);
    }

}
