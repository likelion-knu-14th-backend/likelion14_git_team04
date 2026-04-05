package com.likelion14.session;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final List<MenuResponseDto> menuStore = new ArrayList<>();
    private Long currentId = 1L;

    // 메뉴 등록 POST
    @PostMapping
    public MenuResponseDto createMenu(@RequestBody MenuCreateRequestDto request) {
        MenuResponseDto menu = new MenuResponseDto(
                currentId++,
                request.getName(),
                request.getPrice(),
                request.getCategory()
        );

        menuStore.add(menu);
        return menu;
    }

    // 전체 메뉴 조회 GET
    @GetMapping
    public List<MenuResponseDto> getMenus() {
        return menuStore;
    }

    // 단건 조회 GET - ID 기준
    @GetMapping("/{id}")
    public MenuResponseDto getMenu(@PathVariable Long id) {
        for (MenuResponseDto menu : menuStore) {

            if (menu.getId().equals(id)) {
                return menu;
            }
        }
        return null;
    }

    // 수정 PUT - ID 기준
    @PutMapping("/{id}")
    public MenuResponseDto updateMenu(
            @PathVariable Long id,
            @RequestBody MenuCreateRequestDto request
    ) {
        for (int i = 0; i < menuStore.size(); i++) {
            MenuResponseDto menu = menuStore.get(i);

            if (menu.getId().equals(id)) {
                MenuResponseDto updateMenu = new MenuResponseDto(
                        id,
                        request.getName(),
                        request.getPrice(),
                        request.getCategory()
                );

                menuStore.set(i, updateMenu);
                return updateMenu;
            }
        }
        return null;
    }

    // 삭제 DELETE - ID 기준
    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id) {
        for (int i = 0; i < menuStore.size(); i++) {
            MenuResponseDto menu = menuStore.get(i);

            if (menu.getId().equals(id)) {
                menuStore.remove(i);
                return;
            }
        }
    }
}