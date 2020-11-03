package springboot.o2ov1.web.shopadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springboot.o2ov1.dto.ImageHolder;
import springboot.o2ov1.dto.ProductExecution;
import springboot.o2ov1.entity.Product;
import springboot.o2ov1.entity.ProductCategory;
import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.enums.ProductEnum;
import springboot.o2ov1.service.ProductCategoryService;
import springboot.o2ov1.service.ProductService;
import springboot.o2ov1.util.CodeUtil;
import springboot.o2ov1.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagement {
    // 允许最多上传6张图片
    private final static int IMG_MAX_COUNT = 6;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    // 分页查询
    @RequestMapping(value = "/getallproducts", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllProducts(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        String categoryId = request.getParameter("categoryId");
        // 我们需要将查询的信息整合进product 因为dao是根据product各个属性字段进行查询
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        Product product = new Product();
        product.setShop(shop);
        product.setProductName(productName);
        ProductCategory productCategory = new ProductCategory();
        if (categoryId != null)
            productCategory.setProductCategoryId(Long.decode(categoryId));
        product.setProductCategory(productCategory);

        ProductExecution productExecution = productService.getAllProductsByProduct(product, pageIndex, pageSize);
        if (productExecution.getState() == 1) {
            modelMap.put("products", productExecution.getProducts());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", productExecution.getStateInfo());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproduct", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProduct(@RequestParam("productId") Long id) {
        Map<String, Object> modelMap = new HashMap<>();
        if (id == null || id < -1) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "产品id为空");
            return modelMap;
        }
        ProductExecution productExecution = productService.getProduct(id);
        if (productExecution.getState() == ProductEnum.SUCCESS.getState()) {
            Product product = productExecution.getProduct();
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", productExecution.getStateInfo());
        }
        return modelMap;
    }

    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean isSuccess = CodeUtil.checkVerifyCode(request);
        if (!isSuccess) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String productStr = HttpServletRequestUtil.getString(request, "product");
        ObjectMapper objectMapper = new ObjectMapper();
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgs = new ArrayList<>();
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 若请求中包含文件流，则取出相关的文件（包含缩略图和详情图）
        try {
            if (resolver.isMultipart(request)) {
                thumbnail = getImageHolder((MultipartHttpServletRequest) request, thumbnail, productImgs);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
            if (product != null && thumbnail != null && productImgs.size() > 0) {
                Shop shop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(shop);
                ProductExecution productExecution = productService.addProduct(product, thumbnail, productImgs);
                if (productExecution.getState() == ProductEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "商品信息不完整");
            }
            return modelMap;
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
    }
    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        boolean isSuccess = CodeUtil.checkVerifyCode(request);
        if (!statusChange && !isSuccess) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String productStr = HttpServletRequestUtil.getString(request, "product");
        ObjectMapper objectMapper = new ObjectMapper();
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgs = new ArrayList<>();
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 若请求中包含文件流，则取出相关的文件（包含缩略图和详情图）
        try {
            if (resolver.isMultipart(request)) {
                thumbnail = getImageHolder((MultipartHttpServletRequest) request, thumbnail, productImgs);
            }
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        Product product = null;
        try {
            product = objectMapper.readValue(productStr, Product.class);
            if (product != null) {
                Shop shop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(shop);
                ProductExecution productExecution = productService.modifyProduct(product, thumbnail, productImgs);
                if (productExecution.getState() == ProductEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "商品信息不完整");
            }
            return modelMap;
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
    }

    private ImageHolder getImageHolder(MultipartHttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgs) throws IOException {
        MultipartHttpServletRequest multipartRequest;
        multipartRequest = request;
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (commonsMultipartFile != null) {
            thumbnail = new ImageHolder(commonsMultipartFile.getOriginalFilename(), commonsMultipartFile.getInputStream());
        }
        // 获取商品详情图 并构建List<ImageHolder>
        for (int i = 0; i < IMG_MAX_COUNT; i++) {
            commonsMultipartFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (commonsMultipartFile != null) {
                ImageHolder img = new ImageHolder(commonsMultipartFile.getOriginalFilename(), commonsMultipartFile.getInputStream());
                productImgs.add(img);
            } else {
                break;
            }
        }
        return thumbnail;
    }

}
