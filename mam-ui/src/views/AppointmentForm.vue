<template>
  <div>
    <van-nav-bar
      :title="formTitle"
      left-text="返回"
      @click-left="onClickLeft"
    />
    <van-form @submit="onSubmit" v-if="vm === 0">
      <van-field
        readonly
        clickable
        name="calendar"
        :value="bookDate"
        label="日期"
        placeholder="点击选择日期"
        @click="showCalendar = true"
        :rules="[{ required: true, message: '请选择日期' }]"
      />
      <van-calendar v-model="showCalendar" @confirm="onConfirm" />
      <van-field label="星期" :value="week" readonly />
      <van-field
        readonly
        clickable
        name="datetimePicker"
        :value="startTime"
        label="开始时间"
        placeholder="点击选择时间"
        @click="showStartPicker = true"
        :rules="[{ required: true, message: '请选择开始时间' }]"
      />
      <van-popup v-model="showStartPicker" position="bottom">
        <van-datetime-picker
          type="time"
          @confirm="onStartTimeConfirm"
          @cancel="showStartPicker = false"
        />
      </van-popup>
      <van-field
        readonly
        clickable
        name="datetimePicker"
        :value="endTime"
        label="结束时间"
        placeholder="点击选择时间"
        @click="showEndPicker = true"
        :rules="[{ required: true, message: '请选择结束时间' }]"
      />
      <van-popup v-model="showEndPicker" position="bottom">
        <van-datetime-picker
          type="time"
          @confirm="onEndTimeConfirm"
          @cancel="showEndPicker = false"
        />
      </van-popup>

      <van-field
        readonly
        clickable
        name="picker"
        :value="doctor"
        label="选择医师"
        placeholder="点击选择医师"
        @click="showPicker = true"
        :rules="[{ required: true, message: '请选择医师' }]"
      />
      <van-popup v-model="showPicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="doctors"
          @confirm="onPickerConfirm"
          @cancel="showPicker = false"
        />
      </van-popup>
      <van-field name="stepper" label="最大预约量">
        <template #input>
          <van-stepper v-model="candidate" />
        </template>
      </van-field>
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
    <div v-if="vm === 1">
      <p><van-icon name="warning-o" size="3rem" color="#fa8c16" /></p>
      <h2>请确认以下信息：</h2>
      <p>医生：{{doctor}}</p>
      <p>日期：{{bookDate}} 周{{week}}</p>
      <p>时间：{{startTime}} - {{endTime}}</p>
      <p>最大预约量：{{candidate}}</p>
      <p><span style="font-size:.8rem">发布之后，如果有客户进行了预约则本条信息将无法修改或者删除。</span></p>
      <p>
        <van-button plain @click="reFill">重新填写</van-button>
        <van-button type="primary" @click="publish" :loading="loading">发布信息</van-button>
      </p>
    </div>
  </div>
</template>

<script>
const week2chinese = ["日", "一", "二", "三", "四", "五", "六"];
export default {
  data() {
    return {
      vm: 0,
      loading: false,
      formTitle: "添加信息",
      id: 0,
      bookDate: "",
      week: "",
      showCalendar: false,
      startTime: "",
      showStartPicker: false,
      endTime: "",
      showEndPicker: false,
      doctors: [],
      showPicker: false,
      doctor: "",
      candidate: 60,
      ymd: 0,
      loadedDoctors: false,
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.loadDoctors();
    if (this.id > 0) {
      this.formTitle = "编辑信息";
      let interval = setInterval(() => {
        if (this.loadedDoctors) {
          clearInterval(interval);
          this.loadData();
        }
      }, 200);
    }
  },
  methods: {
    loadData() {
      const url = `/api/system/appointment?id=${this.id}`;
      let toast = this.$toast.loading('加载预约信息');
      this.$h.get(url)
      .cb(data => {
        if (data['status'] === 0) {
          let item = data['item'];
          this.bookDate = item['bookDate'];
          this.week = item['bookWeek'];
          let tr = item['timeRange'].split('-');
          this.startTime = tr[0];
          this.endTime = tr[1];
          this.doctor = item['doctorId'] + '. ' + item['doctor'];
          this.candidate = item['candidateCount'];
          this.ymd = item['ymd'];
        } else {
          this.$notify({type: 'danger', message: decodeURI(data['message'])});
        }
      })
      .fcb(() => toast.clear())
      .req();
    },
    loadDoctors() {
      const url = "/api/system/doctors";
      let toast = this.$toast.loading("加载医师信息");
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            let list = data.list;
            let temp = [];
            for (let i in list) {
              let item = list[i];
              temp[i] = item.id + ". " + item.doctorName;
            }
            this.doctors = temp;
          } else {
            this.$notify({ type: "danger", message: decodeURI(data.message) });
          }
        })
        .fcb(() => {
          this.loadedDoctors = true;
          toast.clear();
        })
        .req();
    },
    onClickLeft() {
      this.$router.push("/appointment");
    },
    onSubmit() {
      const url = "/api/system/appointment";
      const fd = new FormData();
      if (this.id > 0) {
        fd.append("id", this.id);
      }
      let doctorInfo = this.doctor.split(". ");
      fd.append("did", doctorInfo[0]);
      fd.append("doctor", doctorInfo[1]);
      fd.append("date", this.bookDate);
      fd.append("week", this.week);
      fd.append("time", `${this.startTime}-${this.endTime}`);
      fd.append("candidate", this.candidate);
      fd.append("ymd", this.ymd);
      this.loading = true;
      this.$h
        .post(url, fd)
        .cb((data) => {
          if (data.status === 0) {
            this.id = data.item;
            this.$notify({ type: "success", message: "已保存" });
            this.vm = 1;
          } else {
            this.$notify({ type: "danger", message: decodeURI(data.message) });
          }
        })
        .fcb(() => (this.loading = false))
        .req();
    },
    onConfirm(date) {
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let _date = date.getDate();
      let day = date.getDay();
      this.bookDate = `${year}年${month}月${_date}日`;
      this.week = week2chinese[day];
      this.ymd = year * 10000 + month * 100 + _date;
      this.showCalendar = false;
    },
    onStartTimeConfirm(time) {
      this.startTime = time;
      this.showStartPicker = false;
    },
    onEndTimeConfirm(time) {
      this.endTime = time;
      this.showEndPicker = false;
    },
    onPickerConfirm(value) {
      this.doctor = value;
      this.showPicker = false;
    },
    reFill () {
      this.vm = 0;
    },
    publish () {
      const url = `/api/system/appointment/publish`;
      const fd = new FormData();
      fd.append("id", this.id);
      this.loading = true;
      this.$h.post(url, fd)
      .cb(data => {
        if (data.status === 0) {
          this.$notify({type: 'success', message: '已保存'});
        } else {
          this.$notify({type: 'danger', message: decodeURI(data.message)});
        }
      })
      .fcb(() => this.loading = false)
      .req();
    }
  },
};
</script>

<style>
</style>