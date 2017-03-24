/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class SizeGtCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private int value;
/*    */ 
/*    */   public SizeGtCriterion(String propName, int value)
/*    */   {
/* 17 */     if (StringUtils.isEmpty(propName)) {
/* 18 */       throw new QueryException("Property name is null!");
/*    */     }
/* 20 */     this.propName = propName;
/* 21 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 25 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public int getValue() {
/* 29 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 34 */     if (this == other)
/* 35 */       return true;
/* 36 */     if (!(other instanceof SizeGtCriterion))
/* 37 */       return false;
/* 38 */     SizeGtCriterion castOther = (SizeGtCriterion)other;
/* 39 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 45 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 50 */     return "size of " + getPropName() + " > " + this.value;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.SizeGtCriterion
 * JD-Core Version:    0.6.2
 */