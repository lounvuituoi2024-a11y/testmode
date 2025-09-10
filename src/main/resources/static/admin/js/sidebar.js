// Sidebar component
const sidebarHTML = `
<div style="width: 280px; min-height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); position: fixed; left: 0; top: 0; z-index: 1000; transition: all 0.3s ease; color: white;">
    <div style="padding: 20px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1);">
        <h3 style="margin: 0; font-weight: 600;">Admin Panel</h3>
    </div>
    <ul style="list-style: none; padding: 0; margin: 0;">
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/dashboard.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-tachometer-alt" style="margin-right: 10px;"></i> Dashboard
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/users.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-users" style="margin-right: 10px;"></i> Quản lý người dùng
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/xe.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-car" style="margin-right: 10px;"></i> Quản lý xe
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/khachhang.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-users" style="margin-right: 10px;"></i> Khách hàng
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/dichvu.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-tools" style="margin-right: 10px;"></i> Dịch vụ
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="/admin/taikhoan.html" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-user-cog" style="margin-right: 10px;"></i> Tài khoản
            </a>
        </li>
        <li style="border-bottom: 1px solid rgba(255,255,255,0.1);">
            <a href="#" onclick="logout()" style="display: block; padding: 15px 20px; color: rgba(255,255,255,0.8); text-decoration: none; transition: all 0.3s;">
                <i class="fas fa-sign-out-alt" style="margin-right: 10px;"></i> Đăng xuất
            </a>
        </li>
    </ul>
</div>
`;

// Load sidebar
document.addEventListener('DOMContentLoaded', function() {
    const sidebarContainer = document.querySelector('.sidebar');
    if (sidebarContainer) {
        sidebarContainer.innerHTML = sidebarHTML;
        
        // Set active menu item
        const currentPath = window.location.pathname;
        const links = sidebarContainer.querySelectorAll('a');
        
        links.forEach(link => {
            if (link.getAttribute('href') === currentPath || 
                (currentPath.includes('users') && link.getAttribute('href').includes('users'))) {
                link.style.background = 'rgba(255,255,255,0.1)';
                link.style.color = 'white';
                link.style.paddingLeft = '30px';
            }
            
            // Add hover effect
            link.addEventListener('mouseenter', function() {
                if (!this.style.background.includes('rgba(255,255,255,0.1)')) {
                    this.style.background = 'rgba(255,255,255,0.05)';
                    this.style.paddingLeft = '30px';
                }
            });
            
            link.addEventListener('mouseleave', function() {
                if (!this.style.background.includes('rgba(255,255,255,0.1)')) {
                    this.style.background = 'transparent';
                    this.style.paddingLeft = '20px';
                }
            });
        });
    }
});

// Logout function
function logout() {
    if (confirm('Bạn có chắc chắn muốn đăng xuất?')) {
        localStorage.removeItem('adminLoggedIn');
        window.location.href = '/admin/login.html';
    }
}
