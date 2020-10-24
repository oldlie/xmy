<template>
  <div>
    <van-nav-bar title="预约信息" left-text="返回" @click-left="onClickLeft">
      <template #right>
        导出
        <van-icon name="down" size="18" @click="exportInfo" />
      </template>
    </van-nav-bar>

    <div
      style="
        border-box: box-sizing;
        margin: 10px 0;
        padding: 5px 10px;
        font-size: 14px;
      "
    >
      <van-row>
        <van-col :span="8" style="text-algin: left" @click="changeDate"
          ><span>{{ dateFmt }}</span></van-col
        >
        <van-col :span="8" style="text-algin: center" @click="changeDoctor">{{
          doctor
        }}</van-col>
        <van-col :span="8" style="text-algin: right" @click="changeStatus">{{
          canceledTxt
        }}</van-col>
      </van-row>
    </div>

    <van-cell
      v-for="(item, index) in list"
      v-bind:key="index"
      :title="`${item.nickname} ${item.phone}`"
      :value="item.doctor"
      style="text-align: left"
    >
      <template #label>
        <div v-if="item.canceled === 1">
          <van-tag color="#bfbfbf">已取消</van-tag>
        </div>
        <div>预约时段：{{ item.timeRange }}</div>
      </template>
    </van-cell>

    <van-pagination
      v-model="page"
      :total-items="total"
      :items-per-page="size"
      @change="loadData"
    />

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
      currentDate: new Date(),
      doctor: "全部医生",
      dateFmt: "",
      did: 0,
      canceled: 0,
      canceledTxt: "有效",
      page: 1,
      size: 5,
      total: 0,
      ymd: 20201001,
      minDate: new Date(2020, 9, 1),
      list: [],
      showDate: false,
      doctors: [],
      showPicker: false,
      status: ["有效", "全部", "已取消"],
      showPicker2: false,
    };
  },
  mounted() {
    this.dateFmt = this.formatDate(this.currentDate);
    this.ymd = this.getYmd(this.currentDate);
    this.loadData();
  },
  methods: {
    onClickLeft() {
      this.$router.push("/dashboard");
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
    loadData() {
      const url = `/api/system/book-info-list?did=${this.did}&ymd=${this.ymd}&can=${this.canceled}&page=${this.page}&size=${this.size}`;
      let toast = this.$toast.loading("正在加载预约数据");
      this.$h
        .get(url)
        .cb((data) => {
          if (data["status"] === 0) {
            this.total = data["total"];
            this.list = data["list"];
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
    changeDate() {
      this.showDate = true;
    },
    onDateConfirm(date) {
      this.dateFmt = this.formatDate(date);
      this.ymd = this.getYmd(date);
      this.showDate = false;
      this.loadData();
    },
    onPickerConfirm(value) {
      let temp = value.split(". ");
      this.did = temp[0];
      this.doctor = temp[1];
      this.loadData();
      this.showPicker = false;
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
      this.loadData();
      this.showPicker2 = false;
    },
    exportInfo() {
      this.$router.push('/export/book-info');
    }
  },
};
</script>

<style>
</style>