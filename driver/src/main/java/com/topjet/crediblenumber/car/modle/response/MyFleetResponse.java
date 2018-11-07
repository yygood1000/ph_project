package com.topjet.crediblenumber.car.modle.response;

import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;

import java.util.List;

/**
 * Created by tsj-004 on 2017/10/17.
 */

public class MyFleetResponse {
    private List<TruckTeamCar> truck_team_list = null; // 车队列表

    public List<TruckTeamCar> getTruck_team_list() {
        return truck_team_list;
    }

    public void setTruck_team_list(List<TruckTeamCar> truck_team_list) {
        this.truck_team_list = truck_team_list;
    }
}
