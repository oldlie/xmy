<template>
  <div>
    <van-nav-bar title="预约结果" left-text="返回" @click-left="onClickLeft" />

    <div v-if="vm === 0" style="text-align: left; padding: 30px 15px">
      <h2>确认预约信息</h2>
      <van-form @submit="book">
        <van-field
          v-model="nickname"
          name="联系人"
          label="联系人"
          placeholder="预约联系人"
          :rules="nicknameRule"
        />
        <van-field
          v-model="phone"
          name="联系电话"
          label="联系电话"
          placeholder="请填写13位手机号码"
          :rules="phoneRule"
        />
        <van-field readonly label="医生" :value="item.doctor" />
        <van-field readonly label="日期" :value="item.bookDate" />
        <van-field readonly label="星期" :value="`星期${item.bookWeek}`" />
        <van-field readonly label="时段" :value="item.timeRange" />
        <div style="margin: 16px">
          <van-button
            round
            block
            type="info"
            native-type="submit"
            :loading="loading"
            >预约</van-button
          >
        </div>
      </van-form>
    
    </div>

    <div
      v-if="vm === 1"
      style="
        width: 100;
        min-height: 240px;
        text-align: center;
        padding: 80px 10px 0 10px;
      "
    >
      <van-icon name="smile-o" color="#237804" size="3rem" />
      <h3>预约成功</h3>
      <p>医生：{{ item.doctor }}</p>
      <p>日期：{{ item.bookDate }} 星期{{ item.bookWeek }}</p>
      <p>时段：{{ item.timeRange }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      vm: 0,
      nickname: "",
      nicknameRule: [
        { required: true, message: "请填写预约联系人，方便我们联系您" },
      ],
      aid: 0,
      did: 0,
      result: false,
      tips: "",
      item: "",
      phone: "",
      phoneRule: [
        {
          validator: (val) => /^1[3456789]\d{9}$/.test(val),
          message: "请填写13位手机号码",
        },
      ],
      loading: false,
    };
  },
  mounted() {
    this.aid = this.$route.params.aid;
    this.did = this.$route.params.did;
    this.loadPreBookInfo();
  },
  methods: {
    onClickLeft() {
      this.$router.push(`/appointment/${this.did}`);
    },
    loadPreBookInfo() {
      const url = `/api/user/book-pre-info?aid=${this.aid}`;
      let toast = this.$toast.loading("加载预约信息");
      this.$h
        .get(url)
        .cb((data) => {
          console.log("pre info===>", data);
          if (data["status"] === 0) {
            let item = data.item;
            this.nickname = item["nickname"];
            this.phone = item["phone"];
            this.item = item["appointment"];
          }
        })
        .fcb(() => toast.clear())
        .req();
    },
    book() {
      const url = `/api/user/book`;
      const fd = new FormData();
      fd.append("aid", this.aid);
      fd.append("did", this.did);
      fd.append('nn', this.nickname);
      fd.append('ph', this.phone);
      let toast = this.$toast.loading("正在预约");
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data["status"] === 0) {
            this.vm = 1;
            this.result = true;
            this.item = data["item"];
          } else {
            this.$notify({type: 'danger', message: decodeURI(data["message"])});
          }
        })
        .fcb(() => {
          toast.clear();
        })
        .req();
    }
  },
};
</script>

<style>
</style>