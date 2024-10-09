<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="form-container" style="margin-left: -10px;margin-top: -8px ;"> <!-- 左移表单 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
        <el-form-item label="员工工号" prop="jobNumber">
          <el-input v-model="queryParams.jobNumber" placeholder="请输入员工工号" clearable @keyup.enter.native="getList" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="findBySearch">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>
    <!-- 操作日志表格 -->
    <el-table v-loading="loading" :data="list" @selection-change="SelectionChange" style="width: auto;"
      :row-style="{ height: '45px' }" :cell-style="{ padding: '0' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工号" align="center" prop="jobNumber" width="120" />
      <el-table-column label="操作人" align="center" prop="name" />
      <el-table-column label="操作时间" align="center" prop="logTime" width="150">
        <template slot-scope="scope">
          {{ scope.row.logTime | formatTimestamp }}
        </template>
      </el-table-column>
      <el-table-column label="操作类型" align="center" prop="logName" />
      <el-table-column label="操作内容" align="center" prop="remark" />
      <el-table-column label="用户ip" align="center" prop="userIp" width="130"/>
      <el-table-column label="用户所在地" align="center" prop="userAddress" />
      <el-table-column label="用户端口" align="center" prop="userPort" />
      <el-table-column label="主机名" align="center" prop="userHost" width="130"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="margin-top: 5px;width: auto;text-align: center;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum" :page-sizes="[10, 20, 30, 40, 50, 70, 90, 120]"
        :page-size="queryParams.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  data() {
    return {
      //权限
      permission: {
        positionId: null,
        departmentId: null
      },
      // 遮罩层，控制加载状态
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用，当未选中或选中多于一个时，单个操作按钮禁用
      single: true,
      // 非多个禁用，当未选中时，多个操作按钮禁用
      multiple: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1, // 当前页码
        pageSize: 10, // 每页显示条数
        //查询属性
        jobNumber: null,
        queryPayDate: null,
        paymentMethod: null,

      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
    this.reset();
  },
  methods: {
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 导出数据
    handleExport() {
      const filename = `${new Date().getTime()}-操作日志.xlsx`;
      // 解构赋值，排除不需要的分页参数
      this.$download('/log/exportLog', this.queryParams, filename)
        .then(() => {
        }).catch((error) => {
        });
    },
    // 获取列表数据
    getList() {
      this.loading = true;
      request.get("/log/selectLogList", { params: this.queryParams }).then((res) => {
        if (res.code == 200) {
          this.list = res.data.list;
          this.total = res.data.total;
          this.loading = false;
        } else {
          this.loading = false;
          this.$message.error(res.msg);
          this.total = 0;
        }
      });
    },
    //搜索按钮操作
    findBySearch() {
      this.getList();
    },
    // 重置查询参数
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        //查询属性名
        jobNumber: null,
        logName: null,
        paymentMethod: null,

      };
      this.getList();
    },
    // 取消对话框
    cancel() {
      this.reset();
      this.$refs['form'].resetFields(); // 重置表单验证状态
      this.open = false;

    },
    // 重置表单
    reset() {
      this.form = {};
    },
    // 删除信息
    handleDelete(row) {
      const ids = row.id || this.ids;
      if (!row.id) {
        row.name = '大批量';
        row.logName = '日志信息';
      }
      this.$model.confirm(`确认要删除 ${row.name} 的 ${row.logName} 信息吗? `).then(() => {
        return request.delete('/log/deleteLog/' + ids);
      }).then((res) => {
        if (res.code == 200) {
          this.$message.success("删除成功");
          this.getList();
        } else {
          this.$model.msgError(res.msg);
        }
      }).catch((err) => {
        if (err !== 'cancel') { // 过滤掉用户取消操作
          this.$model.msgError("删除失败: " + err.message);
        }
      });
    },
    // 处理选中变化
    SelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = selection.length === 0;
    },
    // 分页设置
    handleSizeChange(pageSize) {
      //改变时会触发	每页条数
      this.queryParams.pageSize = pageSize;
      this.getList();

    },
    handleCurrentChange(pageNum) {
      //改变时会触发	当前页
      this.queryParams.pageNum = pageNum;
      this.getList();
    },
  }
};
</script>

<style scoped></style>