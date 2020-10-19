package cn.xzxy.lewy.quartz.service;

import cn.xzxy.lewy.quartz.dao.TbJobEntityRepository;
import cn.xzxy.lewy.quartz.job.DynamicJob;
import cn.xzxy.lewy.quartz.model.TbJobEntity;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 为了方便控制Job的运行，为调度中心添加相关的业务逻辑
 */
@Service
public class DynamicJobService {

    @Resource
    private TbJobEntityRepository repository;

    //通过Id获取Job
    public TbJobEntity getJobEntityById(Integer id) {
        return repository.getById(id);
    }

    //从数据库中加载获取到所有Job
    public List<TbJobEntity> loadJobs() {
        return (List<TbJobEntity>) repository.findAll();
    }

    //获取JobDataMap.(Job参数对象)
    public JobDataMap getJobDataMap(TbJobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("jobDescription", job.getDescription());
        map.put("vmParam", job.getVmParam());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    //获取Trigger (Job的触发器,执行规则)
    public Trigger getTrigger(TbJobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    //获取JobKey,包含Name和Group
    public JobKey getJobKey(TbJobEntity job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }
}
