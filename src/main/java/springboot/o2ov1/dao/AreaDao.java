package springboot.o2ov1.dao;


import springboot.o2ov1.entity.Area;

import java.util.List;

public interface AreaDao {
    List<Area> selectAll();
}
