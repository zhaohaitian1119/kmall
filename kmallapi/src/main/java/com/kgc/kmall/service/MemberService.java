package com.kgc.kmall.service;

import com.kgc.kmall.bean.Member;
import com.kgc.kmall.bean.MemberReceiveAddress;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-12-15 16:13
 */
public interface MemberService {
    public List<Member> selectAllMember();

    void addUserToken(String token, Long id);

    Member login(Member member);

    Member checkOauthUser(Long sourceUid);

    void addOauthUser(Member umsMember);

    List<MemberReceiveAddress> getReceiveAddressByMemberId(Long memberId);

    MemberReceiveAddress getReceiveAddressById(long l);
}
