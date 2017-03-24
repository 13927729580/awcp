/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class StartsWithTextCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private String value;
/*    */ 
/*    */   public StartsWithTextCriterion(String propName, String value)
/*    */   {
/* 16 */     if (StringUtils.isEmpty(propName)) {
/* 17 */       throw new QueryException("Property name is null!");
/*    */     }
/* 19 */     if (StringUtils.isEmpty(value)) {
/* 20 */       throw new QueryException("Value is null!");
/*    */     }
/* 22 */     this.propName = propName;
/* 23 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 27 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 31 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 36 */     if (this == other)
/* 37 */       return true;
/* 38 */     if (!(other instanceof StartsWithTextCriterion))
/* 39 */       return false;
/* 40 */     StartsWithTextCriterion castOther = (StartsWithTextCriterion)other;
/* 41 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 47 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 52 */     return getPropName() + " like '" + this.value + "*'";
/*    */   }
/*    */ }

