/*    */ package org.szcloud.framework.core.domain;
/*    */ 
/*    */ import java.security.InvalidParameterException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class OrderSetting
/*    */ {
/* 11 */   private boolean ascending = true;
/*    */   private String propName;
/*    */ 
/*    */   public boolean isAscending()
/*    */   {
/* 15 */     return this.ascending;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 19 */     return this.propName;
/*    */   }
/*    */ 
/*    */   private OrderSetting(boolean ascending, String propName) {
/* 23 */     if (StringUtils.isEmpty(propName)) {
/* 24 */       throw new InvalidParameterException("propName should not be empty!");
/*    */     }
/* 26 */     this.ascending = ascending;
/* 27 */     this.propName = propName;
/*    */   }
/*    */ 
/*    */   public static OrderSetting asc(String propName) {
/* 31 */     return new OrderSetting(true, propName);
/*    */   }
/*    */ 
/*    */   public static OrderSetting desc(String propName) {
/* 35 */     return new OrderSetting(false, propName);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 40 */     if (this == other)
/* 41 */       return true;
/* 42 */     if (!(other instanceof OrderSetting))
/* 43 */       return false;
/* 44 */     OrderSetting castOther = (OrderSetting)other;
/* 45 */     return new EqualsBuilder().append(this.ascending, castOther.ascending).append(this.propName, castOther.propName).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 51 */     return new HashCodeBuilder(17, 37).append(this.ascending).append(this.propName).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     return new StringBuilder().append(this.propName).append(this.ascending ? " ascending" : "descending").toString();
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     com.dayatang.domain.OrderSetting
 * JD-Core Version:    0.6.2
 */