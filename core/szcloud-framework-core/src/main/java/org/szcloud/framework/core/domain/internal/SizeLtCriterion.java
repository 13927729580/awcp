/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class SizeLtCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private int value;
/*    */ 
/*    */   public SizeLtCriterion(String propName, int value)
/*    */   {
/* 16 */     if (StringUtils.isEmpty(propName)) {
/* 17 */       throw new QueryException("Property name is null!");
/*    */     }
/* 19 */     this.propName = propName;
/* 20 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 24 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public int getValue() {
/* 28 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 33 */     if (this == other)
/* 34 */       return true;
/* 35 */     if (!(other instanceof SizeLtCriterion))
/* 36 */       return false;
/* 37 */     SizeLtCriterion castOther = (SizeLtCriterion)other;
/* 38 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 44 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     return "size of " + getPropName() + " < " + this.value;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.SizeLtCriterion
 * JD-Core Version:    0.6.2
 */