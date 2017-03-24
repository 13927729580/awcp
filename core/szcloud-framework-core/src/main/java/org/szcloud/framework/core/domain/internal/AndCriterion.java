/*    */ package org.szcloud.framework.core.domain.internal;
/*    */ 
/*    */ import org.szcloud.framework.core.domain.QueryCriterion;
/*    */ import org.szcloud.framework.core.domain.QueryException;
/*    */ 
/*    */ public class AndCriterion
/*    */   implements QueryCriterion
/*    */ {
/*    */   private QueryCriterion[] criterions;
/*    */ 
/*    */   public AndCriterion(QueryCriterion[] criterions)
/*    */   {
/* 10 */     if ((criterions == null) || (criterions.length < 2)) {
/* 11 */       throw new QueryException("At least two query criterions required!");
/*    */     }
/* 13 */     this.criterions = criterions;
/*    */   }
/*    */ 
/*    */   public QueryCriterion[] getCriterons() {
/* 17 */     return this.criterions;
/*    */   }
/*    */ }

