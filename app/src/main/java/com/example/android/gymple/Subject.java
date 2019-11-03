package com.example.android.gymple;

import java.util.ArrayList;

public interface Subject {
    void registerObserver(RepositoryObserver repositoryObserver);
    void removeObserver(RepositoryObserver repositoryObserver);
}