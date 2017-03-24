/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class NotNullCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */ 
/*    */   public NotNullCriterion(String propName)
/*    */   {
/* 16 */     if (StringUtils.isEmpty(propName)) {
/* 17 */       throw new QueryException("Property name is null!");
/*    */     }
/* 19 */     this.propName = propName;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 23 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 28 */     if (this == other)
/* 29 */       return true;
/* 30 */     if (!(other instanceof NotNullCriterion))
/* 31 */       return false;
/* 32 */     NotNullCriterion castOther = (NotNullCriterion)other;
/* 33 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 40 */     return new HashCodeBuilder(17, 37).append(getPropName()).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 45 */     return getPropName() + " is not null ";
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.NotNullCriterion
 * JD-Core Version:    0.6.2
 */