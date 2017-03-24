/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class LtCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private Comparable<?> value;
/*    */ 
/*    */   public LtCriterion(String propName, Comparable<?> value)
/*    */   {
/* 18 */     if (StringUtils.isEmpty(propName)) {
/* 19 */       throw new QueryException("Property name is null!");
/*    */     }
/* 21 */     this.propName = propName;
/* 22 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 26 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public Comparable<?> getValue() {
/* 30 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 35 */     if (this == other)
/* 36 */       return true;
/* 37 */     if (!(other instanceof LtCriterion))
/* 38 */       return false;
/* 39 */     LtCriterion castOther = (LtCriterion)other;
/* 40 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 47 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 52 */     return getPropName() + " < " + this.value;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.LtCriterion
 * JD-Core Version:    0.6.2
 */