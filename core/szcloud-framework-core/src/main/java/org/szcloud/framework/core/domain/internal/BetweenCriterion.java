/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class BetweenCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/*    */   private Comparable<?> from;
/*    */   private Comparable<?> to;
/*    */ 
/*    */   public BetweenCriterion(String propName, Comparable<?> from, Comparable<?> to)
/*    */   {
/* 20 */     if (StringUtils.isEmpty(propName)) {
/* 21 */       throw new QueryException("Property name is null!");
/*    */     }
/* 23 */     if (from == null) {
/* 24 */       throw new QueryException("From value is null!");
/*    */     }
/* 26 */     if (to == null) {
/* 27 */       throw new QueryException("To value is null!");
/*    */     }
/* 29 */     this.propName = propName;
/* 30 */     this.from = from;
/* 31 */     this.to = to;
/*    */   }
/*    */ 
/*    */   public String getPropName() {
/* 35 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public Comparable<?> getFrom() {
/* 39 */     return this.from;
/*    */   }
/*    */ 
/*    */   public Comparable<?> getTo() {
/* 43 */     return this.to;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 48 */     if (this == other)
/* 49 */       return true;
/* 50 */     if (!(other instanceof BetweenCriterion))
/* 51 */       return false;
/* 52 */     BetweenCriterion castOther = (BetweenCriterion)other;
/* 53 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.from, castOther.from).append(this.to, castOther.to).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 62 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.from).append(this.to).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 68 */     return getPropName() + " between " + this.from + " and " + this.to;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.BetweenCriterion
 * JD-Core Version:    0.6.2
 */