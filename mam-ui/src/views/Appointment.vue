<template>
  <div>
    <van-nav-bar title="预约排表" left-text="返回" @click-left="onClickLeft">
      <template #right>
        <van-icon name="plus" size="18" @click="addInfo" />
      </template>
    </van-nav-bar>

    <div v-for="item in appointmentList" v-bind:key="item.id">
      <van-swipe-cell>
        <van-cell
          :label="`医师: ${item.doctor}`"
          :value="`${item.timeRange}`"
          is-link
          :to="`/appointment-form/${item.id}`"
          style="text-align: left"
        >
          <template #title>
            <van-icon v-if="item.published === 0" name="warn-o" color="#fa8c16" />
            {{item.bookDate}} 周{{item.bookWeek}}
          </template>
        </van-cell>
        <template #right>
          <van-button
            square
            text="删除"
            type="danger"
            style="height: 100%"
            @click="deleteInfo(item)"
          />
        </template>
      </van-swipe-cell>
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
      appointmentList: [],
      page: 1,
      size: 20,
      total: 0,
      orderBy: "ymd",
      direct: "desc",
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    onClickLeft() {
      this.$router.push("/dashboard/");
    },
    addInfo() {
      this.$router.push("/appointment-form/0");
    },
    loadData() {
      const url = `/api/system/appointments?page=${this.page}&size=${this.size}&orderBy=${this.orderBy}&direct=${this.direct}`;
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