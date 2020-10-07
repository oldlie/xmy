<template>
  <div>
    <van-nav-bar title="员工列表" left-text="返回" @click-left="onClickLeft">
      <template #right>
        <van-icon name="plus" size="18" @click="addInfo" />
      </template>
    </van-nav-bar>

    <div
      style="text-align: left"
      v-for="(item, index) in doctors"
      v-bind:key="index"
    >
      <van-swipe-cell>
        <van-card
          :desc="item.doctorDesc"
          :thumb="item.doctorHeadPic"
        >
          <template #title>
            {{item.id}}. {{item.doctorName}}
          </template>
          <template #price>
            挂号费：￥ {{ item.copay.amount | formatCopay }}
          </template>
          <template #tags>
            <van-tag plain type="success">{{ item.doctorTitle }}</van-tag>
          </template>
          <template #footer>
            <van-button @click="editInfo(item)">编辑</van-button>
          </template>
        </van-card>
        <template #right>
          <van-button square text="删除" type="danger" style="height:100%" @click="deleteInfo(item)" />
        </template>
      </van-swipe-cell>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      doctors: [],
      loading: false
    };
  },
  mounted() {
    let toast = this.$toast.loading({
      duration: 0,
      message: "加载中...",
      forbidClick: true,
      loadingType: "spinner",
    });
    this.loadDoctors(toast);
  },
  filters: {
    formatCopay(amount) {
      amount += "";
      if (amount.indexOf(".") < 0) {
        amount += ".00";
      }
      return amount;
    },
  },
  methods: {
    onClickLeft() {
      this.$router.push("/dashboard");
    },
    loadDoctors(toast) {
      const url = "/api/system/doctors";
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            this.doctors = data.list;
          } else {
            this.$toast.fail(decodeURIComponent(data.message));
          }
        })
        .fcb(() => {
          toast.clear();
        })
        .req();
    },
    addInfo() {
      this.$router.push("/doctor-form/0");
    },
    editInfo(item) {
      this.$router.push("/doctor-form/" + item.id);
    },
    deleteInfo(item) {
      const url = '/api/system/doctor?id=' + item.id;
      this.loading = true;
      this.$h.delete(url)
      .cb(data => {
        if (data.status === 0) {
          this.doctors = this.doctors.filter(d => d.id != item.id);
          this.$notify({type: 'success', message: '已删除'})
        } else {
          this.$notify({type: 'danger', message:decodeURI(data.message)});
        }
      })
      .fcb(() => this.loading = false)
      .req();
    }
  },
};
</script>

<style>
 .delete-button {
    height: 100%;
  }
</style>