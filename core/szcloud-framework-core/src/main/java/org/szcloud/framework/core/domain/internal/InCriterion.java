/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*    */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*    */ 
/*    */ public class InCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private String propName;
/* 19 */   private Collection<? extends Object> value = Collections.EMPTY_SET;
/*    */ 
/*    */   public InCriterion(String propName, Collection<? extends Object> value)
/*    */   {
/* 23 */     if (StringUtils.isEmpty(propName)) {
/* 24 */       throw new QueryException("Property name is null!");
/*    */     }
/* 26 */     this.propName = propName;
/* 27 */     if (value != null)
/* 28 */       this.value = value;
/*    */   }
/*    */ 
/*    */   public InCriterion(String propName, Object[] value)
/*    */   {
/* 33 */     if (StringUtils.isEmpty(propName)) {
/* 34 */       throw new QueryException("Property name is null!");
/*    */     }
/* 36 */     this.propName = propName;
/* 37 */     if ((value != null) && (value.length > 0))
/* 38 */       this.value = Arrays.asList(value);
/*    */   }
/*    */ 
/*    */   public String getPropName()
/*    */   {
/* 43 */     return this.propName;
/*    */   }
/*    */ 
/*    */   public Collection<? extends Object> getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 52 */     if (this == other)
/* 53 */       return true;
/* 54 */     if (!(other instanceof InCriterion))
/* 55 */       return false;
/* 56 */     InCriterion castOther = (InCriterion)other;
/* 57 */     return new EqualsBuilder().append(getPropName(), castOther.getPropName()).append(this.value, castOther.value).isEquals();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 64 */     return new HashCodeBuilder(17, 37).append(getPropName()).append(this.value).toHashCode();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 69 */     return getPropName() + " in collection [" + collectionToString(this.value) + "]";
/*    */   }
/*    */ 
/*    */   private String collectionToString(Collection<? extends Object> value) {
/* 73 */     return StringUtils.join(value, ",");
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.InCriterion
 * JD-Core Version:    0.6.2
 */