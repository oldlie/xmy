<template>
  <div>
    <van-nav-bar :title="formTitle" left-text="返回" @click-left="onClickLeft" />

    <van-form @submit="onSubmit">
      <van-field
        v-model="name"
        name="姓名"
        label="姓名"
        placeholder="姓名"
        :rules="nameRule"
      />
      <van-field
        v-model="title"
        name="职称"
        label="职称"
        placeholder="职称"
        :rules="titleRule"
      />
      <van-field name="uploader" label="上传照片">
        <template slot="input">
          <van-uploader
            v-model="headPicList"
            multiple
            :max-count="1"
            :max-size="500 * 1024"
            @oversize="onOversize"
            :before-read="beforeRead"
            :after-read="afterRead"
          />
        </template>
      </van-field>
      <van-field
        v-model="desc"
        name="简历"
        label="简历"
        placeholder="简历"
        rows="2"
        type="textarea"
        maxlength="50"
        autosize
        show-word-limit
      />
      <van-field
        v-model="copay"
        label="挂号费"
        placeholder="请输入小数点后两位：1.00"
        :rules="copayRule"
      />
      <div style="margin: 16px">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          :loading="loading"
          >提交</van-button
        >
      </div>
    </van-form>
  </div>
</template>

<script>
export default {
  data() {
    const copayReg = /^\d+\.\d{2}$/;
    const validator = function (val) {
      return copayReg.test(val);
    };
    return {
      formTitle: '添加信息',
      loading: false,
      id: 0,
      title: "",
      name: "",
      nameRule: [{ required: true, message: "请填写用户名" }],
      title: "",
      titleRule: [{ required: true, message: "请填职称" }],
      headPicList: [],
      headPic: "",
      desc: "",
      copay: "0.00",
      copayRule: [{ validator, message: "正确的金额格式：1.00" }],
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    console.log("this.id", this.$route.params.id);
    if (this.id > 0) {
      this.formTitle = "编辑信息";
      this.loadDoctor();
    }
  },
  methods: {
    loadDoctor() {
      const url = "/api/system/doctor?id=" + this.id;
      const toast = this.$toast.loading("加载中");
      this.$h.get(url)
      .cb(data => {
        if (data.status === 0) {
          const doctor = data.item;
          this.name = doctor.doctorName;
          this.title = doctor.doctorTitle;
          this.headPicList = [{url: doctor.doctorHeadPic}];
          this.headPic = doctor.doctorHeadPic;
          this.desc = doctor.doctorDesc;
          let amount = doctor.copay.amount + '';
          if (amount.indexOf('.') < 0) {
            amount += '.00';
          }
          this.copay = amount;
        } else {
          this.$notify({type: 'danger', message: decodeURI(data.message)});
        }
      })
      .fcb(() => toast.clear())
      .req();
    },
    onClickLeft() {
      this.$router.push("/doctor");
    },
    afterRead(file) {
      console.log("file", file);
      const url = "/api/image/upload";
      const fd = new FormData();
      fd.append("file", file.file, file.name);
      const toast = this.$toast.loading("上传中");
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            this.headPic = data.item[0];
            this.headPicList = [ {url: this.headPic} ];
          } else {
            this.$notify({type: 'danger', message: decodeURI(data.message) });
          }
        })
        .fcb(() => {
          toast.clear();
        })
        .req();
      console.log(file);
    },
    onSubmit() {
        const url = '/api/system/doctor';
        const fd = new FormData();
        if (this.id > 0) {
            fd.append("id", this.id);
        }
        fd.append("name", this.name);
        fd.append("title", this.title);
        fd.append("headPic", this.headPic);
        fd.append("desc", this.desc);
        fd.append("copay", "CNY " + this.copay);
        this.loading = true;
        this.$h.post(url, fd)
        .cb(data => {
            if (data.status === 0) {
                this.$toast.success("已保存");
            } else {
                this.$toast.warning(decodeURI(data.message));
            }
        })
        .fcb(() => {
            this.loading = false;
        })
        .req();
    },
    onOversize(file) {
      console.log(file);
      this.$toast("文件大小不能超过 500kb");
    },
    beforeRead(file) {
      if (file.type !== "image/jpeg") {
        this.$toast("请上传 jpg 格式图片");
        return false;
      }
      return true;
    },
  },
};
</script>

<style>
</style>