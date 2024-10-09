<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="form-container" style="margin-left: -10px;margin-top: -8px ;"> <!-- 左移表单 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
        <el-form-item label="员工工号" prop="jobNumber">
          <el-input v-model="queryParams.jobNumber" placeholder="请输入员工工号" clearable @keyup.enter.native="getList" />
        </el-form-item>
        <el-form-item label="支付日期" prop="payDate">
          <el-date-picker v-model="queryParams.queryPayDate" type="month" value-format="yyyy-MM" placeholder="请选择支付日期"
            clearable @keyup.enter.native="getList" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="queryParams.paymentMethod" placeholder="支付方式">
            <el-option label="银行卡" value="银行卡" />
            <el-option label="现金" value="现金" />
            <el-option label="微信" value="微信" />
          </el-select>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5" v-if="(user.positionId >= 2 && user.fatherId == 2) || (user.positionId >= 5)">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>
    <!-- 员工薪资表格 -->
    <el-table v-loading="loading" :data="list" @selection-change="SelectionChange" width="100%"
      :row-style="{ height: '45px' }" :cell-style="{ padding: '0' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工工号" align="center" prop="jobNumber" width="106" />
      <el-table-column label="支付日期" align="center" prop="payDate" width="95" />
      <el-table-column label="基本工资(￥)" align="center" prop="basicSalary" />
      <el-table-column label="加班费(￥)" align="center" prop="overtimePay" width="106" />
      <el-table-column label="奖金(￥)" align="center" prop="bonus" />
      <el-table-column label="提成(￥)" align="center" prop="commission" />
      <el-table-column label="津贴(￥)" align="center" prop="allowance" />
      <el-table-column label="五险一金(￥)" align="center" prop="fiveInsurancesOneFund" />
      <el-table-column label="净工资(￥)" align="center" prop="netSalary" />
      <el-table-column label="银行卡号" align="center" prop="bankCard" width="145" />
      <el-table-column label="支付方式" align="center" prop="paymentMethod" />
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
        :current-page="queryParams.pageNum" :page-sizes="[10, 20, 30, 40]" :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!-- 添加或修改员工对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="员工工号" prop="jobNumber" v-if="jobNumberShow">
          <el-input v-model="form.jobNumber" placeholder="请输入员工工号" />
        </el-form-item>
        <el-form-item label="支付日期" prop="payDate">
          <el-date-picker clearable v-model="form.payDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择支付日期"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="基本工资(￥)" prop="basicSalary">
          <el-input v-model="form.basicSalary" placeholder="请输入基本工资" />
        </el-form-item>
        <el-form-item label="加班费(￥)" prop="overtimePay">
          <el-input v-model="form.overtimePay" placeholder="请输入加班费" />
        </el-form-item>
        <el-form-item label="奖金(￥)" prop="bonus">
          <el-input v-model="form.bonus" placeholder="请输入奖金" />
        </el-form-item>
        <el-form-item label="提成(￥)" prop="commission">
          <el-input v-model="form.commission" placeholder="请输入提成" />
        </el-form-item>
        <el-form-item label="津贴(￥)" prop="allowance">
          <el-input v-model="form.allowance" placeholder="请输入津贴" />
        </el-form-item>
        <el-form-item label="扣款(￥)" prop="deductions">
          <el-input v-model="form.deductions" placeholder="请输入扣款" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="form.paymentMethod" placeholder="请选择支付方式" style="width: 100%;">
            <el-option label="银行卡" value="银行卡" />
            <el-option label="现金" value="现金" />
            <el-option label="微信" value="微信" />
          </el-select>
        </el-form-item>
        <el-form-item label="扣款备注(￥)" prop="remarks ">
          <el-input v-model="form.remarks" placeholder="请输入扣款备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <!-- :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" -->
      <el-upload action="#" ref="upload" :limit="1" accept=".xlsx, .xls" :disabled="upload.isUploading"
        :auto-upload="false" drag :http-request="customUploadHandler">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="uploadClose">取 消</el-button>
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
      //显示工号键入
      jobNumberShow: false,
      user: userCookie,
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
        jobNumbe: null,
        queryPayDate: null,
        paymentMethod: null,

      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 上传的地址
        url: ''
      },
      // 表单参数
      form: {},
      // 表单校验规则
      rules: {
        jobNumber: [
          { required: true, message: "员工工号不能为空", trigger: "blur" },
        ],
        payDate: [
          { required: true, message: "支付日期不能为空", trigger: "change" }
        ],
        basicSalary: [
          { required: true, message: "基本工资不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "基本工资必须是数字，最多两位小数", trigger: "blur" }
        ],
        overtimePay: [
          { required: true, message: "加班费不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "加班费必须是数字，最多两位小数", trigger: "blur" }
        ],
        bonus: [
          { required: true, message: "奖金不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "奖金必须是数字，最多两位小数", trigger: "blur" }
        ],
        commission: [
          { required: true, message: "提成不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "提成必须是数字，最多两位小数", trigger: "blur" }
        ],
        allowance: [
          { required: true, message: "津贴不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "津贴必须是数字，最多两位小数", trigger: "blur" }
        ],
        deductions: [
          { required: true, message: "扣款不能为空", trigger: "blur" },
          { pattern: /^\d+(\.\d{1,2})?$/, message: "扣款必须是数字，最多两位小数", trigger: "blur" }
        ],
        paymentMethod: [
          { required: true, message: "支付方式不能为空", trigger: "change" }
        ],
        bankCard: [
          { required: true, message: "银行卡号不能为空", trigger: "blur" },
          { pattern: /^\d{16,19}$/, message: "银行卡号必须是16到19位数字", trigger: "blur" }
        ],
        remarks: [
          { required: true, message: "扣款备注不能为空", trigger: "blur" },
          { max: 200, message: "扣款备注不能超过200字", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    const userCookie = JSON.parse(this.$cookies.get('user'));
    this.getEmployee(userCookie.jobNumber);
    this.getList();
    this.reset();
  },
  methods: {
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "薪资导入";
      this.upload.open = true;
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles();
      }
    },
    // 取消导入
    uploadClose() {
      this.$refs.upload.clearFiles();
      this.upload.open = false;
    },
    //实际导入函数
    customUploadHandler(option) {
      if ((this.user.positionId >= 3 && this.user.fatherId == 2) || (this.user.positionId >= 5)) {
        const formData = new FormData(); // 创建 FormData 对象来封装文件数据
        formData.append('file', option.file); // 将文件添加到 FormData 对象
        // 发起 POST 请求上传文件
        request.post('/salary/upload', formData).then((res) => {
          if (res.code == 200) {
            this.$model.msgSuccess("上传成功");
            this.getList();
          } else {
            this.$model.msgError(res.msg);
            this.getList();
          }
        }).catch(error => {
          this.$model.msgError(error.message); // 显示错误消息
        });
      } else {
        this.$model.msgError("您没有权限导入薪资信息");
      }
    },
    // 提交上传文件
    submitFileForm() {
      const uploadFiles = this.$refs.upload.uploadFiles;
      if (uploadFiles.length == 1) {
        this.submitFileForm = this.$throttle(this.submitFileForm, 2000, "文件已提交，请勿重复提交");
        uploadFiles.forEach(file => {
          this.customUploadHandler({ file: file.raw }); // 确保传递的是单个文件对象
        });
        this.upload.open = false;
        this.$refs.upload.clearFiles();
        this.getList();
      } else {
        this.$message.error('请选择文件后再上传');
      }
    },
    // 导出数据
    handleExport() {
      const filename = `${new Date().getTime()}-工资数据.xlsx`;
      // 解构赋值，排除不需要的分页参数
      this.$download('/salary/export', this.queryParams, filename)
        .then(() => {
        }).catch((error) => {
        });
    },
    // 获取列表数据
    getList() {
      this.loading = true;
      request.get("/salary/salaryList", { params: this.queryParams }).then((res) => {
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
        jobNumbe: null,
        queryPayDate: null,
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
      this.form = {
      };
      this.queryParams = {
        pageNum: 1, // 当前页码
        pageSize: 10, // 每页显示条数
        //查询属性
        jobNumbe: null,
        payDate: null,
        paymentMethod: null,

      }
    },
    // 新增按钮操作
    handleAdd() {
      this.jobNumberShow = true;
      this.reset();
      this.form.remarks = '无';
      this.open = true;
      if (this.$refs['form']) {
        this.$refs['form'].resetFields(); // 重置表单验证状态
      }
      this.title = "添加员工";
    },
    // 修改按钮操作
    handleUpdate(row) {
      this.jobNumberShow = false;
      this.reset();
      this.form = Object.assign({}, row); // 使用对象复制防止直接修改绑定数据
      this.open = true;
      if (this.$refs['form']) {
        this.$refs['form'].resetFields(); // 重置表单验证状态
      }
      this.title = "修改员工";
    },
    // 提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
            request.put("/salary/salaryUpdate", this.form).then((res) => {
              if (res.code == 200) {
                this.open = false;
                this.$message.success("修改成功");
              } else {
                this.$message.error(res.msg);
              }
            });
            this.getList();
          } else {
            request.post("/salary/salaryInsert", this.form).then((res) => {
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
      const ids = row.id || this.ids;
      this.$model.confirm('确认要删除工号为 ' + ids + ' 员工的薪资信息吗?').then(() => {
        return request.delete('/salary/salaryDelete/' + ids);
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
    getEmployee(jobNumbe) {
      request.get(`/employee/getEmployee?jobNumber=${jobNumbe}`).then((res) => {
        if (res.code == 200) {
          this.user = res.data;
          if (res.data.fatherId != 2 && res.data.positionId < 5) {
            this.$router.push({ path: '/' });
          }
        }else{
          return
        }
      });
    }
  },
  mounted() {
    const userCookie = JSON.parse(this.$cookies.get('user'));
    this.getEmployee(userCookie.jobNumber);
  },
};
</script>

<style scoped></style>