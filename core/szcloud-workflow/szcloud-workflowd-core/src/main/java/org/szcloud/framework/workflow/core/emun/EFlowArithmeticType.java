package org.szcloud.framework.workflow.core.emun;

/*
 * 流转状态算法参数，该参数对整个流转计算有着深刻的影响，要设计合理的流程，
 * 理解该参数是一个非常重要的环节，它设置的不同将直接影响流程的走向，会产生
 * 不同的结果。
 * 说明：如果为ArithmeticCurrentEntry或ArithmeticLastEntry只针对当前处理
 * 的状态所对应的步骤而言，对于非当前步骤，还是以流转的最后（最新）触发状态
 * 所对应的步骤计算进行流转计算，如下所例举：
 * 
 * 假设一个流程有步骤：开始、批示、通知、...等步骤，在流转中已触发如下状态：
 *       + 所有状态<1>
 *          + 开始<2>
 *              开始状态<3>
 *          + 批示<4>
 *              批示状态<5>
 *              批示状态<6>
 *              批示状态<7>
 *              批示状态<8>
 *          + 通知<9>
 *              通知状态<10>
 *              通知状态<11>
 *          + 批示<12>
 *              批示状态<13>
 *              批示状态<14>
 *          + 通知<15>
 *              通知状态<16>
 *              通知状态<17>
 * 此时某一个收件人在处理“批示状态<7>”，那么在计算各步骤的条件时，如果
 * =ArithmeticCurrentEntry，则需计算的状态为：开始<2>，批示<4>，通知<15>
 * =ArithmeticLastEntry，则需计算的状态为：开始<2>，批示<12>，通知<15>
 * 因为其中“批示<4>”其实是当前处理的状态，因此在计算的步骤方面有所不同而已
 */
public class EFlowArithmeticType {
	public static final int ArithmeticAllEntry = 0; //#计算步骤所对应的所有状态
	public static final int ArithmeticCurrentEntry = 1; //#计算与步骤所对应的当前状态同时触发的状态
	public static final int ArithmeticLastEntry = 2; //#计算与步骤最近触发状态同时触发的状态
}
