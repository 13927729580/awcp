/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class ContainsElementCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private Object value;
/*    */ 
/*    */   public ContainsElementCriterion(String propName, Object value)
/*    */   {
/* 18 */     if (StringUtils.isEmpty(propName)) {
/* 19 */       throw new QueryException("Property name is null!");
/*    */     }
/* 21 */     if (value == null) {
/* 22 */       throw new QueryException("Value is null!");
/*    */     }
/* 24 */     this.propName = propName;
/* 25 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 29 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public Object getValue() {
/* 33 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 38 */     if (this == other)
/* 39 */       return true;
/* 40 */     if (!(other instanceof ContainsElementCriterion))
/* 41 */       return false;
/* 42 */     ContainsElementCriterion castOther = (ContainsElementCriterion)other;
/* 43 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 50 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     return getPropName() + " contains " + this.value;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.ContainsElementCriterion
 * JD-Core Version:    0.6.2
 */