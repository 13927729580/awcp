/*     */ package org.szcloud.framework.core.domain;
/*     */ 
/*     */ import org.szcloud.framework.core.domain.internal.AndCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.BetweenCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.ContainsTextCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.EqCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.EqPropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.GeCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.GePropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.GtCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.GtPropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.InCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.IsEmptyCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.IsNullCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.LeCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.LePropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.LtCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.LtPropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotEmptyCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotEqCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotEqPropCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotInCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.NotNullCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.OrCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeEqCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeGeCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeGtCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeLeCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeLtCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.SizeNotEqCriterion;
/*     */ import org.szcloud.framework.core.domain.internal.StartsWithTextCriterion;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class Criterions
/*     */ {
/*     */   public static QueryCriterion eq(String propName, Object value)
/*     */   {
/*  47 */     return new EqCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notEq(String propName, Object value) {
/*  51 */     return new NotEqCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion ge(String propName, Comparable<?> value) {
/*  55 */     return new GeCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion gt(String propName, Comparable<?> value) {
/*  59 */     return new GtCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion le(String propName, Comparable<?> value) {
/*  63 */     return new LeCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion lt(String propName, Comparable<?> value) {
/*  67 */     return new LtCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion eqProp(String propName1, String propName2) {
/*  71 */     return new EqPropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notEqProp(String propName1, String propName2) {
/*  75 */     return new NotEqPropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion gtProp(String propName1, String propName2) {
/*  79 */     return new GtPropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion geProp(String propName1, String propName2) {
/*  83 */     return new GePropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion ltProp(String propName1, String propName2) {
/*  87 */     return new LtPropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion leProp(String propName1, String propName2) {
/*  91 */     return new LePropCriterion(propName1, propName2);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeEq(String propName, int size) {
/*  95 */     return new SizeEqCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeNotEq(String propName, int size) {
/*  99 */     return new SizeNotEqCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeGt(String propName, int size) {
/* 103 */     return new SizeGtCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeGe(String propName, int size) {
/* 107 */     return new SizeGeCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeLt(String propName, int size) {
/* 111 */     return new SizeLtCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion sizeLe(String propName, int size) {
/* 115 */     return new SizeLeCriterion(propName, size);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion containsText(String propName, String value) {
/* 119 */     return new ContainsTextCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion startsWithText(String propName, String value) {
/* 123 */     return new StartsWithTextCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion in(String propName, Collection<?> value) {
/* 127 */     return new InCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion in(String propName, Object[] value) {
/* 131 */     return new InCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notIn(String propName, Collection<?> value) {
/* 135 */     return new NotInCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notIn(String propName, Object[] value) {
/* 139 */     return new NotInCriterion(propName, value);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion between(String propName, Comparable<?> from, Comparable<?> to) {
/* 143 */     return new BetweenCriterion(propName, from, to);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion isNull(String propName) {
/* 147 */     return new IsNullCriterion(propName);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notNull(String propName) {
/* 151 */     return new NotNullCriterion(propName);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion isEmpty(String propName) {
/* 155 */     return new IsEmptyCriterion(propName);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion notEmpty(String propName) {
/* 159 */     return new NotEmptyCriterion(propName);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion not(QueryCriterion criterion) {
/* 163 */     return new NotCriterion(criterion);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion and(QueryCriterion[] criterions) {
/* 167 */     return new AndCriterion(criterions);
/*     */   }
/*     */ 
/*     */   public static QueryCriterion or(QueryCriterion[] criterions) {
/* 171 */     return new OrCriterion(criterions);
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     org.szcloud.framework.core.domain.Criterions
 * JD-Core Version:    0.6.2
 */