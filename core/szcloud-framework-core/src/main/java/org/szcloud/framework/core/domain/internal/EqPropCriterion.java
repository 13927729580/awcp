/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class EqPropCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName1;
/*    */   private String propName2;
/*    */ 
/*    */   public EqPropCriterion(String propName1, String propName2)
/*    */   {
/* 17 */     if ((StringUtils.isEmpty(propName1)) || (StringUtils.isEmpty(propName2))) {
/* 18 */       throw new QueryException("One of property name is null!");
/*    */     }
/* 20 */     this.propName1 = propName1;
/* 21 */     this.propName2 = propName2;
/*    */   }
/*    */ 
/*    */   public String getPropName1() {
/* 25 */     return this.propName1;
/*    */   }
/*    */ 
/*    */   public String getPropName2() {
/* 29 */     return this.propName2;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 34 */     if (this == other)
/* 35 */       return true;
/* 36 */     if (!(other instanceof EqPropCriterion))
/* 37 */       return false;
/* 38 */     EqPropCriterion castOther = (EqPropCriterion)other;
/* 39 */     return new EqualsBuilder().append(getPropName1(), castOther.getPropName1()).append(this.propName2, castOther.propName2).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 46 */     return new HashCodeBuilder(17, 37).append(getPropName1()).append(this.propName2).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     return getPropName1() + " = " + this.propName2;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.EqPropCriterion
 * JD-Core Version:    0.6.2
 */