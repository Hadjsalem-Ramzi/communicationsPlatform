package com.ts.plateformcommunication.services;

import com.ts.plateformcommunication.dto.ForumDto;

import java.util.List;

public interface ForumService {
    public ForumDto save(ForumDto forumDto);

    public ForumDto findById(Long id);

    public ForumDto findByName(String name);

    public List<ForumDto> findAll();

    public ForumDto update(ForumDto forumDto);

    public String delete(Long id);
}
