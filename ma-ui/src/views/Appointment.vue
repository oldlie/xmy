<template>
  <div>
    <van-nav-bar title="预约排表" left-text="返回" @click-left="onClickLeft" />

    <div v-for="item in appointmentList" v-bind:key="item.id">
      <div v-if="Number(item.bookedCount) < Number(item.candidateCount)">
        <van-cell
          :title="`${item.bookDate} 周${item.bookWeek}`"
          :value="`${item.timeRange}`"
          is-link
          :to="`/book-result/${did}/${item.id}`"
          style="text-align: left"
        >
          <template #label>
            <div style="color: #262626">
              <div>医师: {{ item.doctor }}</div>
              <div>
                预约量：{{ item.bookedCount }}/{{ item.candidateCount }}
              </div>
            </div>
          </template>
        </van-cell>
      </div>
      <div v-else>
        <van-cell :value="`${item.timeRange}`" style="text-align: left">
          <template #title>
            <div>
              <van-tag color="#bfbfbf">已约满</van-tag> {{ item.bookDate }} 
            </div>
          </template>
          <template #label>
            <div>医师: {{ item.doctor }}</div>
            <div>预约量：{{ item.bookedCount }}/{{ item.candidateCount }}</div>
          </template>
        </van-cell>
      </div>
    </div>

    <van-pagination
      v-model="page"
      :total-items="total"
      :items-per-page="size"
      @change="loadData"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      did: 0,
      appointmentList: [],
      page: 1,
      size: 20,
      total: 0,
      orderBy: "ymd",
      direct: "desc",
    };
  },
  mounted() {
    this.did = this.$route.params.id;
    this.loadData();
  },
  methods: {
    onClickLeft() {
      this.$router.push("/doctor");
    },
    loadData() {
      const url = `/pub/appointments?did=${this.did}&page=${this.page}&size=${this.size}&ob=${this.orderBy}&direct=${this.direct}`;
      let toast = this.$toast.loading("加载预约信息");
      this.$h
        .get(url)
        .cb((data) => {
          if (data["status"] === 0) {
            this.total = data["total"];
            this.appointmentList = data["list"];
          } else {
            this.$notify({
              type: "error",
              messsage: decodeURI(data["message"]),
            });
          }
        })
        .fcb(() => toast.clear())
        .req();
    },
    deleteInfo(item) {
      const url = `/api/system/appointment?id=${item.id}`;
      let toast = this.$toast.loading("正在删除");
      this.$h
        .delete(url)
        .cb((data) => {
          if (data["status"] === 0) {
            this.appointmentList = this.appointmentList.filter(
              (x) => x.id != item.id
            );
            this.$notify({ type: "success", message: "已删除" });
          } else {
            this.$notify({
              type: "danger",
              messsage: decodeURI(data["message"]),
            });
          }
        })
        .fcb(() => toast.clear())
        .req();
    },
  },
};
</script>

<style>
</style>