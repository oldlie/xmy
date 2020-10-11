<template>
  <div>
    <van-nav-bar title="湘美雅皮肤门诊预约">
      <template #left>
        <van-icon @click="navigate('/')" name="wap-home-o" size="24px" />
      </template>
    </van-nav-bar>

    <van-tabs v-model="active">
      <van-tab title="手机短信登录">
        <van-form @submit="onSmsSubmit">
          <van-field
            v-model="phone"
            name="手机号"
            label="手机号"
            placeholder="手机号"
            :rules="phoneRule"
          />
          <van-field
            v-model="sms"
            center
            clearable
            label="获取登录码"
            placeholder="请输入登录码"
          >
            <template #button>
              <van-button
                size="small"
                type="primary"
                @click="preSend"
                native-type="button"
                >获取登录码</van-button
              >
            </template>
          </van-field>
          <div style="margin: 16px">
            <van-button
              round
              block
              type="info"
              native-type="submit"
              :loading="loading"
              >登录</van-button
            >
          </div>
        </van-form>
      </van-tab>
      <!--van-tab title="账号密码登录">
        <van-form @submit="onSubmit">
          <van-field
            v-model="username"
            name="用户名"
            label="用户名"
            placeholder="用户名"
            :rules="usernameRule"
          />
          <van-field
            v-model="password"
            type="password"
            name="密码"
            label="密码"
            placeholder="密码"
            :rules="passwordRule"
          />
          <div style="margin: 16px">
            <van-button
              round
              block
              type="info"
              native-type="submit"
              :loading="loading"
              >登录</van-button
            >
          </div>
        </van-form>
      </van-tab-->
    </van-tabs>

    <van-dialog
      v-model="show"
      title="请输入验证码"
      show-cancel-button
      @confirm="sendCaptcha"
    >
      <img @click="refresh" :src="captcha" />
      <van-field v-model="captchaCode" placeholder="请输入验证码" />
    </van-dialog>
  </div>
</template>

<script>
export default {
  data() {
    let phoneValidator = function (val) {
      let reg = /^1[3456789]\d{9}$/;
      return reg.test(val);
    };
    return {
      active: 0,
      captcha: this.$http.defaults.baseURL + "/pub/kaptcha",
      captchaCode: "",
      phone: "",
      phoneRule: [
        { required: true, message: "请填写手机号" },
        { validator: phoneValidator, message: "请填写13位手机号码" },
      ],
      sms: "",
      show: false,
      username: "",
      usernameRule: [{ required: true, message: "请填写用户名" }],
      password: "",
      passwordRule: [{ required: true, message: "请填写密码" }],
      loading: false,
    };
  },
  mounted() {},
  methods: {
    navigate(url) {
      this.$router.push(url);
    },
    onSubmit() {
      const url = "/login";
      const fd = new FormData();
      fd.append("username", this.username);
      fd.append("password", this.password);
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            const token = data.item;
            this.$cookie.set("token", token);
            this.$router.push("/dashboard");
          }
        })
        .fcb(() => this.loading = false)
        .req();
    },
    onSmsSubmit() {
      const url = "/login";
      const fd = new FormData();
      fd.append("username", this.phone);
      fd.append("password", this.sms);
      fd.append("loginType", "phone");
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            const token = data.item;
            this.$cookie.set("token", token); 
            this.$router.back(-1);
          } else {
            this.$notify({type: 'danger', message: decodeURI(data['message'])});
          }
        })
        .fcb(() => this.loading = false)
        .req();
    },
    preSend() {
      this.refresh();
      this.show = true;
    },
    refresh() {
      this.captcha += `?v=${new Date().getTime()}`;
    },
    sendCaptcha() {
      const url = `/pub/send-sms`;
      const fd = new FormData();
      fd.append("phone", this.phone);
      fd.append("code", this.captchaCode);
      this.show = true;
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data["status"] === 0) {
            this.$notify({ type: "success", message: `您的登录码是：${data.item}` });
            this.show = false;
          } else {
            this.$notify({
              type: "danger",
              message: decodeURI(data["message"]),
            });
          }
        })
        .req();
    },
  },
};
</script>

<style>
</style>