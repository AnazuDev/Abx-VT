uname_os_check() (
  os=$1
  case "$os" in
    linux) return 0 ;;
    android) return 0 ;;
    windows) return 0 ;;
  esac
  return 1
)
