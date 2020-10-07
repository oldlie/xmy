<template>
  <div>
    <van-nav-bar title="医师列表" left-text="返回" @click-left="onClickLeft" />

    <div
      style="text-align: left"
      v-for="(item, index) in doctors"
      v-bind:key="index"
    >
        <van-card
          :desc="item.doctorDesc"
          :thumb="item.doctorHeadPic"
        >
          <template #title>
            {{item.doctorName}}
          </template>
          <template #price>
            挂号费：￥ {{ item.copay.amount | formatCopay }}
          </template>
          <template #tags>
            <van-tag plain type="success">{{ item.doctorTitle }}</van-tag>
          </template>
          <template #footer>
            <van-button @click="book(item)">预约</van-button>
          </template>
        </van-card>
       
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      doctors: []
    }
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
  mounted () {
    this.loadData();
  },
  methods: {
    onClickLeft () {
      this.$router.push('/');
    },
    loadData() {
      const url = `/pub/doctors`;
      let toast = this.$toast.loading('加载医师列表');
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            this.doctors = data.list;
          } else {
            this.$notify({type: 'danger', message: decodeURI(data['message'])});
          }
        })
        .fcb(() => {
          toast.clear();
        })
        .req();
    },
    book (item) {
      this.$router.push(`/appointment/${item.id}`);
    }
  }
}
</script>

<style>

</style>