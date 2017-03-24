/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ 
/*    */ public class NotCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private QueryCriterion criterion;
/*    */ 
/*    */   public NotCriterion(QueryCriterion criterion)
/*    */   {
/* 10 */     if (criterion == null) {
/* 11 */       throw new QueryException("Query criterion is null!");
/*    */     }
/* 13 */     this.criterion = criterion;
/*    */   }
/*    */ 
/*    */   public QueryCriterion getCriteron() {
/* 17 */     return this.criterion;
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.internal.NotCriterion
 * JD-Core Version:    0.6.2
 */