package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.UpdateInfo;
import com.bankapp.mybank.Model.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateInfoRepository extends JpaRepository<UpdateInfo, Long> {
    public UpdateInfo getUpdateInfoByUpdateType(UpdateType type);
}
