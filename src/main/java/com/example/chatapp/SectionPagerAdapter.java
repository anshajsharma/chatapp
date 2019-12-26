package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapp.Fragments.ChatsFragment;
import com.example.chatapp.Fragments.FriendsFragment;
import com.example.chatapp.Fragments.RequestFragment;

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0 :
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1 :
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 2 :
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chats";
            case 1:
                return "Friends";
            case 2:
                return "Friend Requests";
            default:
                return null;
        }
    }
}
