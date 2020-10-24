<template>
  <div>
    <van-nav-bar
      title="导出预约信息"
      left-text="返回"
      @click-left="onClickLeft"
    />

    <div
      style="
        border-box: box-sizing;
        margin: 10px 0;
        padding: 5px 10px;
        font-size: 14px;
      "
    >
      <div
        style="margin: 10px 0; padding: 5px 10px; text-algin: center"
        @click="changeDate(0)"
      >
        <span>选择开始日期：{{ startFmt }}</span>
      </div>
      <div
        style="margin: 10px 0; padding: 5px 10px; text-algin: center"
        @click="changeDate(1)"
      >
        <span>选择结束日期：{{ endFmt }}</span>
      </div>
      <div
        style="margin: 10px 0; padding: 5px 10px; text-algin: center"
        @click="changeDoctor"
      >
        <span>选择医生：{{ doctor }}</span>
      </div>
      <div
        style="margin: 10px 0; padding: 5px 10px; text-algin: center"
        @click="changeStatus"
      >
        <span>{{ canceledTxt }}</span>
      </div>

      <van-button
        type="primary"
        size="large"
        :loading="loading"
        @click="exportInfo"
        >导出</van-button
      >

      <p v-if="taskFinished">
        <a :href="downloadUrl" download="预约表.xlsx">下载导出文件</a>
      </p>
    </div>
    <van-calendar
      v-model="showDate"
      @confirm="onDateConfirm"
      :min-date="minDate"
    />
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="doctors"
        @cancel="showPicker = false"
        @confirm="onPickerConfirm"
      />
    </van-popup>
    <van-popup v-model="showPicker2" round position="bottom">
      <van-picker
        show-toolbar
        :columns="status"
        @cancel="showPicker2 = false"
        @confirm="onPicker2Confirm"
      />
    </van-popup>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dateType: 0,
      startYmd: 0,
      startFmt: "",
      endYmd: 0,
      endFmt: "",
      did: 0,
      doctor: "",
      canceled: -1,
      canceledTxt: "全部",
      showDate: false,
      minDate: new Date(2020, 9, 1),
      showPicker: false,
      doctors: [],
      status: ["有效", "全部", "已取消"],
      showPicker2: false,
      loading: false,
      downloadUrl: "",
      task: {},
      taskFinished: false,
    };
  },
  methods: {
    onClickLeft() {
      this.$router.push("/book-info");
    },
    changeDate(flag) {
      this.dateType = flag;
      this.showDate = true;
    },
    changeDoctor() {
      const url = `/api/system/doctors`;
      let toast = this.$toast.loading("加载医生列表");
      this.$h
        .get(url)
        .cb((data) => {
          if (data["status"] === 0) {
            let temp = [];
            for (let key in data["list"]) {
              let item = data["list"][key];
              temp.push(`${item.id}. ${item.doctorName}`);
            }
            temp.push("0. 全部医生");
            this.doctors = temp;
            this.showPicker = true;
          } else {
            this.$notify({
              type: "danger",
              message: decodeURI(data["message"]),
            });
          }
        })
        .fcb(() => toast.clear())
        .req();
    },
    onPickerConfirm(value) {
      let temp = value.split(". ");
      this.did = temp[0];
      this.doctor = temp[1];
      this.showPicker = false;
    },
    changeStatus() {
      this.showPicker2 = true;
    },
    onPicker2Confirm(value) {
      if (value === "有效") {
        this.canceled = 0;
      } else if (value === "已取消") {
        this.canceled = 1;
      } else {
        this.canceled = -1;
      }
      this.canceledTxt = value;
      this.showPicker2 = false;
    },
    onDateConfirm(date) {
      if (this.dateType === 0) {
        this.startFmt = this.formatDate(date);
        this.startYmd = this.getYmd(date);
      } else if (this.dateType === 1) {
        this.endFmt = this.formatDate(date);
        this.endYmd = this.getYmd(date);
      }
      this.showDate = false;
    },
    formatDate(date) {
      if (date instanceof Date) {
        let y = date.getFullYear();
        let m = date.getMonth() + 1;
        let d = date.getDate();
        return `${y}年${m}月${d}日`;
      }
      return "";
    },
    getYmd(date) {
      if (date instanceof Date) {
        let y = date.getFullYear();
        let m = date.getMonth() + 1;
        let d = date.getDate();
        return y * 10000 + m * 100 + d;
      }
      return 0;
    },
    exportInfo() {
      const url = `/api/file/export/book-info?\
did=${this.did}&start=${this.startYmd}&end=${this.endYmd}&canceled=${this.canceled}`;
      this.loading = true;
      this.taskFinished = false;
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            let item = data.item;
            this.downloadUrl = item.url;
            this.task = item.task;
            this.queryStatus(this.task);
          } else {
            this.$notify({ type: "danger", message: decodeURI(data.message) });
            this.loading = false;
          }
        })
        .req();
    },
    queryStatus(task) {
      console.log("queryStatus==>", task);
      const url = `/api/file/export/status?id=${task.id}`;
      let maxQueryCount = 100;
      let interval = setInterval(() => {
        if (maxQueryCount <= 0) {
          clearInterval(interval);
        }
        this.$h
          .get(url)
          .cb((data) => {
            if (data.status === 0) {
              let item = data.item;
              if (item == null) {
                clearInterval(interval);
                this.$notify({
                  type: "danger",
                  message: "任务已经移除",
                });
              } else if (item.status > 1) {
                clearInterval(interval);
                this.loading = false;
                if (item.status === 2) {
                  this.taskFinished = true;
                  this.$notify({
                    type: "success",
                    message: decodeURI(data.message),
                  });
                } else {
                  this.$notify({
                    type: "danger",
                    message: decodeURI(data.message),
                  });
                }
              }
            } else {
              this.loading = false;
              this.$notify({
                type: "danger",
                message: decodeURI(data.message),
              });
            }
          })
          .req();
        maxQueryCount--;
      }, 3000);
    },
  },
};
</script>

<style>
</style>