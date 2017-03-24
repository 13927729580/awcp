/*     */ package org.szcloud.framework.core.domain;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*     */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*     */ 
/*     */ public class QuerySettings<T>
/*     */ {
/*     */   private Class<T> entityClass;
/*     */   private String rootAlias;
/*     */   private int firstResult;
/*     */   private int maxResults;
/*  27 */   private Map<String, String> aliases = new LinkedHashMap();
/*  28 */   private Set<QueryCriterion> criterions = new HashSet();
/*  29 */   private List<OrderSetting> orderSettings = new ArrayList();
/*     */ 
/*     */   public static <T extends Entity> QuerySettings<T> create(Class<T> entityClass) {
/*  32 */     return new QuerySettings(entityClass);
/*     */   }
/*     */ 
/*     */   public static <T extends Entity> QuerySettings<T> create(Class<T> entityClass, String alias) {
/*  36 */     return new QuerySettings(entityClass, alias);
/*     */   }
/*     */ 
/*     */   private QuerySettings(Class<T> entityClass)
/*     */   {
/*  41 */     this.entityClass = entityClass;
/*     */   }
/*     */ 
/*     */   public QuerySettings(Class<T> entityClass, String alias) {
/*  45 */     this.entityClass = entityClass;
/*  46 */     this.rootAlias = alias;
/*     */   }
/*     */ 
/*     */   public Class<T> getEntityClass()
/*     */   {
/*  53 */     return this.entityClass;
/*     */   }
/*     */ 
/*     */   public String getRootAlias() {
/*  57 */     return this.rootAlias;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getAliases() {
/*  61 */     return this.aliases;
/*     */   }
/*     */ 
/*     */   public Set<QueryCriterion> getCriterions() {
/*  65 */     return this.criterions;
/*     */   }
/*     */ 
/*     */   public int getFirstResult() {
/*  69 */     return this.firstResult;
/*     */   }
/*     */ 
/*     */   public int getMaxResults() {
/*  73 */     return this.maxResults;
/*     */   }
/*     */ 
/*     */   public List<OrderSetting> getOrderSettings() {
/*  77 */     return this.orderSettings;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> alias(String propName, String aliasName) {
/*  81 */     this.aliases.put(propName, aliasName);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> eq(String propName, Object value) {
/*  86 */     addCriterion(Criterions.eq(propName, value));
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notEq(String propName, Object value) {
/*  91 */     addCriterion(Criterions.notEq(propName, value));
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> ge(String propName, Comparable<?> value) {
/*  96 */     addCriterion(Criterions.ge(propName, value));
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> gt(String propName, Comparable<?> value) {
/* 101 */     addCriterion(Criterions.gt(propName, value));
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> le(String propName, Comparable<?> value) {
/* 106 */     addCriterion(Criterions.le(propName, value));
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> lt(String propName, Comparable<?> value) {
/* 111 */     addCriterion(Criterions.lt(propName, value));
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> eqProp(String propName, String otherProp) {
/* 116 */     addCriterion(Criterions.eqProp(propName, otherProp));
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notEqProp(String propName, String otherProp) {
/* 121 */     addCriterion(Criterions.notEqProp(propName, otherProp));
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> gtProp(String propName, String otherProp) {
/* 126 */     addCriterion(Criterions.gtProp(propName, otherProp));
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> geProp(String propName, String otherProp) {
/* 131 */     addCriterion(Criterions.geProp(propName, otherProp));
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> ltProp(String propName, String otherProp) {
/* 136 */     addCriterion(Criterions.ltProp(propName, otherProp));
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> leProp(String propName, String otherProp) {
/* 141 */     addCriterion(Criterions.leProp(propName, otherProp));
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeEq(String propName, int size) {
/* 146 */     addCriterion(Criterions.sizeEq(propName, size));
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeNotEq(String propName, int size) {
/* 151 */     addCriterion(Criterions.sizeNotEq(propName, size));
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeGt(String propName, int size) {
/* 156 */     addCriterion(Criterions.sizeGt(propName, size));
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeGe(String propName, int size) {
/* 161 */     addCriterion(Criterions.sizeGe(propName, size));
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeLt(String propName, int size) {
/* 166 */     addCriterion(Criterions.sizeLt(propName, size));
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> sizeLe(String propName, int size) {
/* 171 */     addCriterion(Criterions.sizeLe(propName, size));
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> containsText(String propName, String value) {
/* 176 */     addCriterion(Criterions.containsText(propName, value));
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> startsWithText(String propName, String value) {
/* 181 */     addCriterion(Criterions.startsWithText(propName, value));
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> in(String propName, Collection<? extends Object> value) {
/* 186 */     addCriterion(Criterions.in(propName, value));
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> in(String propName, Object[] value) {
/* 191 */     addCriterion(Criterions.in(propName, value));
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notIn(String propName, Collection<? extends Object> value) {
/* 196 */     addCriterion(Criterions.notIn(propName, value));
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notIn(String propName, Object[] value) {
/* 201 */     addCriterion(Criterions.notIn(propName, value));
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   public <E> QuerySettings<T> between(String propName, Comparable<E> from, Comparable<E> to) {
/* 206 */     addCriterion(Criterions.between(propName, from, to));
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> isNull(String propName) {
/* 211 */     addCriterion(Criterions.isNull(propName));
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notNull(String propName) {
/* 216 */     addCriterion(Criterions.notNull(propName));
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> isEmpty(String propName) {
/* 221 */     addCriterion(Criterions.isEmpty(propName));
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> notEmpty(String propName) {
/* 226 */     addCriterion(Criterions.notEmpty(propName));
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> not(QueryCriterion criterion) {
/* 231 */     addCriterion(Criterions.not(criterion));
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> and(QueryCriterion[] criterions) {
/* 236 */     addCriterion(Criterions.and(criterions));
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> or(QueryCriterion[] criterions) {
/* 241 */     addCriterion(Criterions.or(criterions));
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   private void addCriterion(QueryCriterion criterion) {
/* 246 */     this.criterions.add(criterion);
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> setFirstResult(int firstResult) {
/* 250 */     this.firstResult = firstResult;
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> setMaxResults(int maxResults) {
/* 255 */     this.maxResults = maxResults;
/* 256 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> asc(String propName) {
/* 260 */     this.orderSettings.add(OrderSetting.asc(propName));
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   public QuerySettings<T> desc(String propName) {
/* 265 */     this.orderSettings.add(OrderSetting.desc(propName));
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 272 */     if (this == other)
/* 273 */       return true;
/* 274 */     if (!(other instanceof QuerySettings))
/* 275 */       return false;
/* 276 */     QuerySettings castOther = (QuerySettings)other;
/* 277 */     return new EqualsBuilder().append(this.entityClass, castOther.entityClass).append(this.criterions, castOther.criterions).append(this.firstResult, castOther.firstResult).append(this.maxResults, castOther.maxResults).append(this.orderSettings, castOther.orderSettings).isEquals();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 288 */     return new HashCodeBuilder(17, 37).append(this.entityClass).append(this.criterions).append(this.firstResult).append(this.maxResults).append(this.orderSettings).toHashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     StringBuilder result = new StringBuilder();
/* 300 */     result.append("Class:").append(this.entityClass.getSimpleName()).append(SystemUtils.LINE_SEPARATOR);
/* 301 */     result.append("criterions: [");
/* 302 */     for (QueryCriterion criteron : this.criterions) {
/* 303 */       result.append(criteron);
/*     */     }
/* 305 */     result.append("]").append(SystemUtils.LINE_SEPARATOR);
/* 306 */     result.append(new StringBuilder().append("firstResult:").append(this.firstResult).toString()).append(SystemUtils.LINE_SEPARATOR);
/* 307 */     result.append(new StringBuilder().append("maxResults").append(this.maxResults).toString()).append(SystemUtils.LINE_SEPARATOR);
/* 308 */     result.append("orderSettings: [");
/* 309 */     for (OrderSetting orderSetting : this.orderSettings) {
/* 310 */       result.append(orderSetting);
/*     */     }
/* 312 */     result.append("]");
/* 313 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\dayatang-commons-domain-3.5.jar
 * Qualified Name:     com.dayatang.domain.QuerySettings
 * JD-Core Version:    0.6.2
 */