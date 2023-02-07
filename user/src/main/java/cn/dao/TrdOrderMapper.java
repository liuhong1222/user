package cn.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.entity.TrdOrder;
import main.java.cn.domain.PackageDomain;

@Mapper
public interface TrdOrderMapper {
	
	List<TrdOrder> findByOrderNo(String orderNo);
	
	List<TrdOrder> findByClOrderNo(String clOrderNo);
	
	List<TrdOrder> findByCreUserId(Integer creUserId);
	
	int saveTrdOrder(TrdOrder trdOrder);
	
	int updateTrdOrder(TrdOrder trdOrder);
	
    List<Map<String,Object>> pageFindTrdOrderByCreUserId(Map<String,Object> map);
    
    Integer quertCountTrdOrder(Integer creUserId);
    
    BigDecimal getSumMoney(Integer creUserId);
    
    BigDecimal quertCountTrdOrderByProductsId(Integer creUserId,String productids);
    
    List<PackageDomain> getPayPackage(Map<String,Object> param);
    
    List<Map<String,Object>> getSummyOrderList(Map<String,Object> param);
    
    int getUserIsPresent(String creUserId);
}
