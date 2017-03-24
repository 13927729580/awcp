/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class GtPropCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName1;
/*    */   private String propName2;
/*    */ 
/*    */   public GtPropCriterion(String propName1, String propName2)
/*    */   {
/* 16 */     if ((StringUtils.isEmpty(propName1)) || (StringUtils.isEmpty(propName2))) {
/* 17 */       throw new QueryException("One of property name is null!");
/*    */     }
/* 19 */     this.propName1 = propName1;
/* 20 */     this.propName2 = propName2;
/*    */   }
/*    */ 
/*    */   public String getPropName1() {
/* 24 */     return this.propName1;
/*    */   }
/*    */ 
/*    */   public String getPropName2() {
/* 28 */     return this.propName2;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 33 */     if (this == other)
/* 34 */       return true;
/* 35 */     if (!(other instanceof GtPropCriterion))
/* 36 */       return false;
/* 37 */     GtPropCriterion castOther = (GtPropCriterion)other;
/* 38 */     return new EqualsBuilder().append(getPropName1(), castOther.getPropName1()).append(this.propName2, castOther.propName2).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 45 */     return new HashCodeBuilder(17, 37).append(getPropName1()).append(this.propName2).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 50 */     return getPropName1() + " > " + this.propName2;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.GtPropCriterion
 * JD-Core Version:    0.6.2
 */