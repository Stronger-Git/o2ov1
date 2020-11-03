package springboot.o2ov1.service;


import springboot.o2ov1.entity.Headline;

import java.util.List;

public interface HeadlineService {
    public static final String HEADLINE_KEY = "headline";
    List<Headline> getHeadlineList(Headline headline);
}
