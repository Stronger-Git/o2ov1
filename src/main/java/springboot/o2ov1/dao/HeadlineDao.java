package springboot.o2ov1.dao;


import springboot.o2ov1.entity.Headline;

import java.util.List;

public interface HeadlineDao {
    List<Headline> selectByHeadline(Headline headline);
}
