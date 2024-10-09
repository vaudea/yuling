<template>
  <div class="app-container">
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>
    <!-- 员工薪资表格 -->
    <el-table v-loading="loading" :data="list"  width="100%"
      :row-style="{ height: '45px' }" :cell-style="{ padding: '0' }">
      <el-table-column label="部门名称" align="center" prop="departmentName"  />
      <el-table-column label="部门负责人" align="center" prop="managerName"  />
      <el-table-column label="部门总人数" align="center" prop="count" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="margin-top: 5px;width: auto;text-align: center;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!-- 添加或修改员工对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="部门名称" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门负责人" prop="managerId">
          <el-select v-model="form.managerId" placeholder="请选择部门负责人" style="width: 100%;">
            <el-option v-for="item in people" :label="item.name" :value="item.jobNumber" :key="item.jobNumber" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  data() {
    const userCookie = JSON.parse(this.$cookies.get('user'));
    return {
      user: userCookie,
      // 遮罩层，控制加载状态
      loading: false,
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
        pageSize: 5, // 每页显示条数
        //查询属性
      },
      // 表单参数
      form: {},
      people: {},
      // 表单校验规则
      rules: {
        departmentName: [
          { required: true, message: "部门名称不能为空", trigger: "blur" },
        ],
        managerId: [
          //{ required: true, message: "部门负责人不能为空", trigger: "change" }
        ],
      },
    };
  },
  created() {
    this.getList();
    this.reset();
  },
  methods: {
    // 获取部门高级管理人员信息
    getPeople() {
      request.get("/department/administration/getPeople").then((res) => {
        if (res.code == 200) {
          this.people = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 获取列表数据
    getList() {
      this.loading = true;
      request.get("/department/administration/getList", { params: this.queryParams }).then((res) => {
        if (res.code == 200) {
          // console.log(res.data.list);
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
    // 取消对话框
    cancel() {
      this.reset();
      this.$refs['form'].resetFields(); // 重置表单验证状态
      this.open = false;

    },
    // 重置表单
    reset() {
      this.form = {
      };
      this.queryParams = {
        pageNum: 1, // 当前页码
        pageSize: 5, // 每页显示条数
      }
    },
    // 新增按钮操作
    handleAdd() {
      this.getPeople();
      this.reset();
      this.form.remarks = '无';
      this.open = true;
      if (this.$refs['form']) {
        this.$refs['form'].resetFields(); // 重置表单验证状态
      }
      this.title = "添加部门";
    },
    // 修改按钮操作
    handleUpdate(row) {
      this.getPeople();
      this.reset();
      this.form = Object.assign({}, row); // 使用对象复制防止直接修改绑定数据
      this.open = true;
      if (this.$refs['form']) {
        this.$refs['form'].resetFields(); // 重置表单验证状态
      }
      this.title = "修改部门信息";
    },
    // 提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.departmentId != null) {
            this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
            request.put("/department/administration/updateDepartment", this.form).then((res) => {
              if (res.code == 200) {
                this.open = false;
                this.$message.success("修改成功");
              } else {
                this.$message.error(res.msg);
              }
            });
            this.getList();
          } else {
            request.post("/department/administration/insertDepartment", this.form).then((res) => {
              if (res.code == 200) {
                this.open = false;
                this.$message.success("新增成功");
              } else {
                this.$message.error(res.msg);
              }
            });
            this.getList();
          }
        }
      });

    },
    // 删除信息
    handleDelete(row) {
      this.$model.confirm(`确定要删除该部门信息吗?`).then(() => {
        return request.delete('/department/administration/deleteDepartment/' + row.departmentId);
      }).then((res) => {
        if (res.code == 200) {
          this.$message.success("删除成功");
          this.getList();
        }else{
          this.$model.msgError(res.msg);
        }
      }).catch((err) => {
        if (err !== 'cancel') { // 过滤掉用户取消操作
          this.$model.msgError("删除失败: " + err.message);
        }
      });
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
  },
};
</script>

<style scoped></style>