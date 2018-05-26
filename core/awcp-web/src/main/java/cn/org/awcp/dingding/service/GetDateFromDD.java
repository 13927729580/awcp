package cn.org.awcp.dingding.service;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.dingding.utils.DdRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GetDateFromDD {
       @Autowired
      private SzcloudJdbcTemplate jdbcTemplate;
        /**
         * 将前一天的打卡数据插入到数据库
         *
         *
          * */
       public  void  writeAttandanceRecord(String workDateFrom,String workDateTo) {
        try{
            int len;
            String resultMap = DdRequest.getSignUpRecord( workDateFrom,workDateTo);
            JSONObject result = JSON.parseObject(resultMap);
            List<Map<String,Object>> resultList = (List<Map<String, Object>>) result.get("recordresult");
             len = resultList.size()-1;
            int cycles = (int)Math.ceil(len/100.0) ;
            for(int i=0;i<cycles;i++){
                String    value="";
                String sql = "insert into dd_attandance_record(ID,userId,groupId,checkType,locationResult,"
                        +"workDate,baseCheckTime,userCheckTime,timeResult,recordId,DDid) value ";
                for (int j=i*100;j<100*(i+1);j++){
                    if(j>len){
                        break;
                    }
                    value  += "( UUID(),'"+ resultList.get(j).get("userId")+"','"+ resultList.get(j).get("groupId")+"','"+ resultList.get(j).get("checkType")+"','"
                            + resultList.get(j).get("locationResult")+"',date_format(FROM_UNIXTIME("+ resultList.get(j).get("workDate")+"/1000),'%Y-%m-%d'),"
                            +"date_format(FROM_UNIXTIME("+ resultList.get(j).get("baseCheckTime")+"/1000),'%Y-%m-%d %H:%i:%s'),"
                            +"date_format(FROM_UNIXTIME("+ resultList.get(j).get("userCheckTime")+"/1000),'%Y-%m-%d %H:%i:%s'),'"
                            + resultList.get(j).get("timeResult")+"','"+ resultList.get(j).get("recordId") +"','"
                            + resultList.get(j).get("id")+"'),";
                }
                value = value.substring(0,value.length()-1);
                jdbcTemplate.update(sql+value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void writeAttandanceRecord(){
        Date date = org.apache.commons.lang3.time.DateUtils.addDays(new Date(),-1);
        String dateStr = DateUtils.format(date,"yyyy-MM-dd");
        String workDateFrom = dateStr+" 00:00:00";
        String workDateTo = dateStr +" 23:59:59";
         writeAttandanceRecord(workDateFrom,workDateTo);
    }

    /***
     *
     * 每月1号根据上月打卡出勤情况生成工资详情
     * */
    public  void insertMonthSalary(){
        Date date = org.apache.commons.lang3.time.DateUtils.addMonths(new Date(),-1);
        String dateStr = DateUtils.format(date,"yyyy-mm");
        int a  =jdbcTemplate.update("insert into ddtym_emp_month_salary(ID,user_id,deptid,basic_salary,target_attandance,real_attandance,attandance_salary,add_award,add_meal_support,add_other,expect_sum_salary,minus_social_insurance,minus_public_reserve_fund,minus_taxable_income,minus_tax_amount,real_sum_salary,belongmonth,state)select UUID(),user_id,deptid,basic_salary,target_attandance,0,0,add_award,add_meal_support,add_other,0,minus_social_insurance,minus_public_reserve_fund,minus_taxable_income,minus_tax_amount,real_sum_salary,?,'0' from ddtym_emp_basic_salary_info",date);
        int b = jdbcTemplate.update("update ddtym_emp_month_salary t1 right join (select count(a.ID) as count,b.user_id,substr(workDate,1,7) as workDate from dd_attandance_record a left join p_un_user_base_info b on a.userId=b.user_id_card_number where a.timeResult<>'NotSigned' and checkType='OnDuty' and workDate like concat(?,'%') group by b.user_id) t2 on t1.user_id = t2.user_id and t1.belongmonth=t2.workDate set t1.real_attandance=t2.count where t1.belongmonth=?",date,date);//只要上班打卡过就视为当天出勤了
        List<Map<String ,Object>> ids= jdbcTemplate.queryForList("select ID from ddtym_emp_month_salary");
        for(int i=0;i<ids.size();i++){
            Map<String,Object> map = jdbcTemplate.queryForMap("select basic_salary,target_attandance,real_attandance,add_award,add_meal_support,add_other,minus_social_insurance,minus_public_reserve_fund from ddtym_emp_month_salary where ID=?",ids.get(i).get("ID"));
            double  basic_salary = (Double) map.get("basic_salary");
            double target_attandance = (Double) map.get("target_attandance");
            double real_attandance =  (Double)map.get("real_attandance");
            double add_award =  (Double)map.get("add_award");
            double add_meal_support = (Double) map.get("add_meal_support");
            double add_other =  (Double)map.get("add_other");
            double minus_social_insurance = (Double) map.get("minus_social_insurance");
            double minus_public_reserve_fund = (Double) map.get("minus_public_reserve_fund");
            double attandance_salary = target_attandance==0?1:real_attandance/target_attandance*basic_salary;
            double expect_sum_salary = attandance_salary+add_award+add_meal_support+add_other;
            double minus_taxable_income = expect_sum_salary-minus_social_insurance-minus_public_reserve_fund;
            double minus_tax_amount;
            if(minus_taxable_income<=3500){
                minus_tax_amount = 0;
            }else if(minus_taxable_income<=5000){
                minus_tax_amount = (minus_taxable_income-3500)*0.03;
            }else if(minus_taxable_income<=8500){
                minus_tax_amount = (minus_taxable_income-3500)*0.1-105;
            }else if(minus_taxable_income<12500){
                minus_tax_amount = (minus_taxable_income-3500)*0.2-555;
            }else{
                minus_tax_amount = (minus_taxable_income-3500)*0.25-1005;
            }
            double real_sum_salary = minus_taxable_income-minus_tax_amount;
            jdbcTemplate.update("update ddtym_emp_month_salary set attandance_salary=?,expect_sum_salary=?,minus_taxable_income=?,minus_tax_amount=?,real_sum_salary=? where ID=?",attandance_salary,expect_sum_salary,minus_taxable_income,minus_tax_amount,real_sum_salary,ids.get(i).get("ID"));
        }
    }
}
