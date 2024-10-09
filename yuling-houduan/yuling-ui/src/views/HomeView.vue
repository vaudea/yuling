<template>
    <div id="home">
      <el-row justify="center">
        <el-col :span="24" class="announcements-container">
          <!-- 遍历公告列表 -->
          <el-timeline>
            <h1 style="text-align: left; font-weight: bold;padding-left: 28px;">系统公告</h1>
            <el-timeline-item
              v-for="item in noticeList"
              :key="item.id"
              :timestamp="item.createTime"
              placement="top">
              <el-card class="announcement-card">
                <div class="card-content">
                  <div class="announcement-header">
                    <h2>{{ item.noticeTitle }}</h2>
                  </div>
                  <div class="announcement-body">
                    <div class="announcement-details">
                      <div v-html="item.noticeContent"></div>
                    </div>
                    <div class="announcement-department">
                      <h3>{{ item.unitDepartment }}</h3>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-col>
      </el-row>
      <router-view />
    </div>
  </template>
  
  <script>
  import request from "@/utils/request";
  export default {
    name: "Home",
    data() {
      return {
        noticeList: [],
        queryParams: {
          pageNum: 1,
          pageSize: 3,
          noticeTitle: null,
          noticeType: null,
          noticeContent: null,
          unitDepartment: null,
          status: '0'
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        request.get("/notice/getNotice", { params: this.queryParams }).then(res => {
          if (res.code == 200) {
            console.log(res.data.list);
            this.noticeList = res.data.list;
            this.total = res.data.total;
            this.loading = false;
          }
        });
        this.loading = false;
      }
    }
  }
  </script>
  
  <style scoped>
  #home {
    max-width: 100%;
  }
  
  .announcements-container {
    display: flex;
    flex-direction: column;
  }
  
  .el-timeline-item {
    display: flex;
    justify-content: flex-start; /* 时间轴项左对齐 */
  }
  
  .announcement-card {
    width: 80%;
    min-width: 800px;
    max-width: 100%;
    margin: 0 auto;
    text-align: left; /* 公告内容左对齐 */
  }
  
  .card-content {
    display: flex;
    flex-direction: column;
  }
  
  .announcement-body {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }
  
  .announcement-details {
    flex-grow: 1;
    max-width: 80%;
  }
  
  .announcement-department {
    text-align: right;
    font-size: 14px;
    color: #666;
    margin-left: 20px;
    white-space: nowrap; /* 防止发布单位换行 */
  }
  
  @media (max-width: 800px) {
    .announcement-card {
      width: 100%;
      min-width: 100%;
    }
    .announcement-body {
      flex-direction: column;
      align-items: flex-start;
    }
    .announcement-department {
      margin-left: 0;
      margin-top: 10px;
      text-align: left;
    }
  }
  </style>
  