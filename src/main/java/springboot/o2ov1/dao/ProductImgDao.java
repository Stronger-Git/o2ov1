package springboot.o2ov1.dao;


import springboot.o2ov1.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    int insertBatch(List<ProductImg> productImgs);
    int deleteById(Long productId);
    List<ProductImg> selectAllById(Long productId);
}
