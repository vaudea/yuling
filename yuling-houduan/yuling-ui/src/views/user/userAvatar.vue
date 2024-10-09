<template>
  <div>
    <div class="user-info-head" @click="editCropper()"><el-image :src="options.img" title="点击上传头像"
        class="img-circle img-lg" /></div>
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body @opened="modalOpened"
      @close="closeDialog">
      <el-row>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <vue-cropper ref="cropper" :img="options.img" :info="true" :autoCrop="options.autoCrop"
            :autoCropWidth="options.autoCropWidth" :autoCropHeight="options.autoCropHeight" :fixedBox="options.fixedBox"
            :outputType="options.outputType" @realTime="realTime" v-if="visible" />
        </el-col>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img" />
          </div>
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :sm="3" :xs="3">
          <el-upload action="#" accept=".png,.jpg" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
            <el-button size="small">
              选择
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{ span: 1, offset: 2 }" :sm="2" :xs="2">
          <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{ span: 2, offset: 6 }" :sm="2" :xs="2">
          <el-button type="primary" size="small" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>


<script>
import store from "@/store";
//一个基于 Vue.js 的图像裁剪组件
import { VueCropper } from "vue-cropper";
import request from '@/utils/request';

export default {
  components: { VueCropper },
  data() {
    return {
      open: false,
      visible: false,
      title: "修改头像",
      options: {
        img: store.getters.avatar,  //裁剪图片的地址
        autoCrop: true,             // 是否默认生成截图框
        autoCropWidth: 200,         // 默认生成截图框宽度
        autoCropHeight: 200,        // 默认生成截图框高度
        fixedBox: true,             // 固定截图框大小 不允许改变
        outputType: "png",           // 默认生成截图为PNG格式
        avatarName: store.getters.avatarName, //文件名称
        // 文件名称
      },
      previews: {},
      avatar: '', //传给后台的图片地址
      jobNumber: ''
    };

  },
  created() {
    this.jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
    this.options.img = localStorage.getItem('avatar');
    this.options.avatarName = localStorage.getItem('avatarName');
  },
  methods: {
    // 编辑头像
    editCropper() {
      this.open = true;
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
      this.$nextTick(() => {
        if (this.$refs.cropper) {
          this.$refs.cropper.refresh();
        }
      });
    },
    // 刷新组件
    refresh() {
      this.$refs.cropper.refresh();
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.img = reader.result;
          this.options.avatarName = file.name
          console.log(file);
        };

      }
    },
    // 上传图片
    uploadImg() {
      //调用裁剪工具，将裁剪好的图片放入到data中传输给formData
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("avatarFile", data, this.options.avatarName);
        formData.append("jobNumber", this.jobNumber);
        console.log(data);
        console.log(this.options.avatarName);
        console.log(formData);
        
        // 调用接口上传头像
        request.post("/files/user/avatar", formData).then((res) => {
          if (res.code == 200) {
            this.avatar = res.data;
            this.options.img = "/api/yuling/files/getAvatar/" + res.data;
            this.options.avatarName = res.data.split('-').pop()
            store.commit('SET_AVATAR', this.options.img);
            store.commit('SET_AVATARNAME', this.options.avatarName);
            localStorage.setItem('avatar', this.options.img);
            localStorage.setItem('avatarName', this.options.avatarName);
            this.$model.msgSuccess("修改成功");
            this.updateAvatar(this.jobNumber);
          } else {
            this.$model.msgError("修改失败");
          }
        });
        this.open = false;
        this.visible = false;
      });
    },
    // 实时预览
    realTime(data) {
      console.log(data);
      this.previews = data;
    },
    // 关闭窗口
    closeDialog() {
      this.options.img = localStorage.getItem('avatar');
      this.visible = false;
      window.removeEventListener("resize", this.resizeHandler)
    },
    //将图片路径修改传输到后台
    updateAvatar() {
      // 将修改后的头像地址提交到后台
      request.put("/employee/updateAvatar", {
        jobNumber: this.jobNumber,
        avatar: this.avatar
      }).then((res) => {
      });
    },


  },
  mounted() {

  },
};
</script>